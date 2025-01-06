"use client"

import React, {useState} from "react";
import {Contract} from "@/types/types";
import {getCookie} from "@/utils/cookies";
import {validRoles} from "@/services/global";
import {DataTable} from "@/components/common/DataTable";
import ConfirmDialog from "@/components/common/ConfirmDialog";
import ContractForm from "@/components/contracts/ContractsForm";
import {useContracts} from "@/components/contracts/ContractContext";

const ContractList = ({ roles=[] }: { roles: validRoles[] }) => {
    const { contracts, setContracts } = useContracts();
    const isAdmin: boolean = roles.includes("ROLE_ADMIN");

    const [error, setError] = useState<string | null>(null);
    const [isDialogOpen, setIsDialogOpen] = useState<boolean>(false);
    const [isEditDialogOpen, setIsEditDialogOpen] = useState<boolean>(false);
    const [contractToEdit, setContractToEdit] = useState<Contract | undefined>(undefined);

    const openEditDialog = (contract: Contract): void => {
        setContractToEdit(contract);
        setIsEditDialogOpen(true);
    };

    const openDeleteDialog = (contract: Contract): void => {
        setContractToEdit(contract);
        setIsDialogOpen(true);
    };

    const handleDelete = async () => {
        if (contractToEdit) {
            try {
                const authToken = getCookie("authToken") || "";
                const url = `http://localhost:8080/api/contracts/${contractToEdit.id}`;
                const response = await fetch(url, {
                    method: "DELETE",
                    headers: {
                        Authorization: `Bearer ${authToken}`,
                    },
                });

                if (response.ok) {
                    setContracts(
                        (prevContracts: Contract[]): Contract[] => prevContracts.filter((contract: Contract): boolean => contract.id !== contractToEdit.id)
                    );
                } else {
                    setError(`Failed to delete contract: ${response.statusText}`);
                }
            } catch (error: any) {
                setError(`Error deleting contract: ${error.message}`);
            } finally {
                setContractToEdit(undefined);
            }
        }
        setIsDialogOpen(false);
    };

    const handleGenerateInvoice = async (contractId: number): Promise<void> => {
        try {
            const issueDate = new Date().toISOString().split("T")[0]; // Current date
            const dueDate = new Date();
            dueDate.setDate(dueDate.getDate() + 30); // Due date 30 days later
            const dueDateString = dueDate.toISOString().split("T")[0];

            const invoiceRequest = {
                contractId,
                issueDate,
                dueDate: dueDateString,
                amount: 1000.0,
                paid: false,
            };

            const url: string = `http://localhost:8080/api/invoices`;
            const authToken: string = getCookie("authToken") || "";
            const response = await fetch(url, {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                    Authorization: `Bearer ${authToken}`,
                },
                body: JSON.stringify(invoiceRequest),
            });

            if (response.ok) {
                alert("Invoice generated successfully.");
            } else {
                const errorText = `Failed to generate invoice: ${response.status} ${response.statusText}`;
                setError(errorText);
            }
        } catch (error: any) {
            setError(`Error generating invoice: ${error.message}`);
        }
    };

    const columns = [
        { accessorKey: "id", header: "Contract ID" },
        { accessorKey: "startDate", header: "Start Date" },
        { accessorKey: "endDate", header: "End Date" },
        { accessorKey: "monthlyRent", header: "Monthly Rent (Kč)" },
        { accessorKey: "tenant", header: "Tenant", cell: ({ row }: { row: { original: Contract } }) => (
            <span>{`${row.original.tenant.firstName} ${row.original.tenant.surname}`}</span>
        )},
        { accessorKey: "property", header: "Property", cell: ({ row }: { row: { original: Contract } }) => (
            <span>{row.original.property.name}</span>
        )},
    ];

    if (isAdmin) {
        columns.push({
            accessorKey: "actions", header: "Actions",
            cell: ({ row }: { row: { original: Contract } }) => (
                <ConfirmDialog
                    title="Are you sure?"
                    confirmLabel="Delete"
                    cancelLabel="Cancel"
                    description="This action cannot be undone. It will permanently delete the property."
                    isOpen={isDialogOpen} onConfirm={handleDelete}
                    onCancel={(): void => setIsDialogOpen(false)}
                    trigger={
                        <div className="flex space-x-4">
                            <button onClick={(): Promise<void> => handleGenerateInvoice(row.original.id)} className="text-green-600 hover:text-green-800">
                                Generate Invoice
                            </button>
                            <button onClick={(): void => openEditDialog(row.original)} className="text-blue-600 hover:text-blue-800">
                                Edit
                            </button>
                            <button onClick={(): void => openDeleteDialog(row.original)} className="text-red-600 hover:text-red-800">
                                Delete
                            </button>
                        </div>
                    }
                />
            )
        });
    }

    return (
        <div className="max-w-5xl mx-auto mt-10 p-6 bg-white shadow-md rounded-lg">
            <h1 className="text-3xl font-bold text-center text-gray-800 mb-6">Contracts List</h1>
            {error && (
                <div className="mb-4 p-3 text-red-800 bg-red-100 border border-red-300 rounded">
                    {error}
                </div>
            )}
            {isEditDialogOpen && (
                <div className="fixed inset-0 bg-gray-900 bg-opacity-50 flex items-center justify-center z-50">
                    <div className="bg-white rounded-lg shadow-lg w-full max-w-2xl p-6 relative">
                        <button onClick={() => setIsEditDialogOpen(false)}
                                className="absolute top-3 right-3 text-gray-500 hover:text-gray-700 focus:outline-none">
                            ✖
                        </button>
                        <h2 className="text-2xl font-bold text-center mb-6">Create new property</h2>
                        <ContractForm setIsOpen={setIsEditDialogOpen} contractToEdit={contractToEdit} />
                    </div>
                </div>
            )}

            <DataTable columns={columns} data={contracts} />
            <ConfirmDialog title="Are you sure?"
                           confirmLabel="Delete"
                           cancelLabel="Cancel"
                           description="This action cannot be undone. It will permanently delete the contract."
                           isOpen={isDialogOpen}
                           onConfirm={handleDelete}
                           onCancel={() => setIsDialogOpen(false)} />
        </div>
    );
};

export default ContractList;