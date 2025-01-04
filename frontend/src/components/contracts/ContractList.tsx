"use client"

import React, {useState} from "react";
import {Contract, Tenant} from "@/types/types";
import {getCookie} from "@/utils/cookies";
import {DataTable} from "@/components/common/DataTable";
import ConfirmDialog from "@/components/common/ConfirmDialog";
import ContractForm from "@/components/contracts/ContractsForm";
import {useContracts} from "@/components/contracts/ContractContext";

const ContractList = () => {
    const { contracts, setContracts } = useContracts();

    const [error, setError] = useState<string | null>(null);
    const [isDialogOpen, setIsDialogOpen] = useState<boolean>(false);
    const [isEditDialogOpen, setIsEditDialogOpen] = useState<boolean>(false);
    const [contractToEdit, setContractToEdit] = useState<Contract | undefined>(null);

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
                setContractToEdit(null);
            }
        }
        setIsDialogOpen(false);
    };

    const columns = [
        { accessorKey: "id", header: "Contract ID" },
        { accessorKey: "startDate", header: "Start Date" },
        { accessorKey: "endDate", header: "End Date" },
        { accessorKey: "monthlyRent", header: "Monthly Rent (Kč)" },
        {
            accessorKey: "tenant", header: "Tenant",
            cell: ({ row }: { row: { original: Contract } }) => (
                <span>{`${row.original.tenant.firstName} ${row.original.tenant.surname}`}</span>
            ),
        },
        {
            accessorKey: "property", header: "Property",
            cell: ({ row }: { row: { original: Contract } }) => (
                <span>{row.original.property.name}</span>
            ),
        },
        {
            id: "actions", header: "Actions",
            cell: ({row}: { row: { original: Contract } }) => (
                <ConfirmDialog
                    title="Are you sure?"
                    description="This action cannot be undone. It will permanently delete the property."
                    confirmLabel="Delete"
                    cancelLabel="Cancel"
                    isOpen={isDialogOpen}
                    onConfirm={handleDelete}
                    onCancel={(): void => setIsDialogOpen(false)}
                    trigger={
                        <div className="flex space-x-4">
                            <button onClick={(): void => openEditDialog(row.original)} className="text-blue-600 hover:text-blue-800">
                                Edit
                            </button>
                            <button onClick={(): void => openDeleteDialog(row.original)} className="text-red-600 hover:text-red-800">
                                Delete
                            </button>
                        </div>
                    }
                />
            ),
        }
    ];

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