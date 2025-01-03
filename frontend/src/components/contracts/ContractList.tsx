"use client"


import {DataTable} from "@/components/common/DataTable";
import ConfirmDialog from "@/components/common/ConfirmDialog";
import {Contract} from "@/types/types";
import {useEffect, useState} from "react";
import {getCookie} from "@/utils/cookies";
import {useContracts} from "@/components/contracts/ContractContext";

const ContractList = () => {
    const { contracts, setContracts } = useContracts();

    const [error, setError] = useState<string | null>(null);
    const [isDialogOpen, setIsDialogOpen] = useState<boolean>(false);
    const [contractToDelete, setContractToDelete] = useState<Contract | null>(null);


    const handleDelete = async () => {
        if (contractToDelete) {
            try {
                const authToken = getCookie("authToken") || "";
                const url = `http://localhost:8080/api/contracts/${contractToDelete.id}`;
                const response = await fetch(url, {
                    method: "DELETE",
                    headers: {
                        Authorization: `Bearer ${authToken}`,
                    },
                });

                if (response.ok) {
                    setContracts((prevContracts) =>
                        prevContracts.filter((contract) => contract.id !== contractToDelete.id)
                    );
                } else {
                    setError(`Failed to delete contract: ${response.statusText}`);
                }
            } catch (error: any) {
                setError(`Error deleting contract: ${error.message}`);
            } finally {
                setContractToDelete(null);
            }
        }
        setIsDialogOpen(false);
    };

    const openDeleteDialog = (contract: Contract) => {
        setContractToDelete(contract);
        setIsDialogOpen(true);
    };

    const columns = [
        { accessorKey: "id", header: "Contract ID" },
        { accessorKey: "startDate", header: "Start Date" },
        { accessorKey: "endDate", header: "End Date" },
        { accessorKey: "monthlyRent", header: "Monthly Rent (Kč)" },
        {
            accessorKey: "tenant",
            header: "Tenant",
            cell: ({ row }: { row: { original: Contract } }) => (
                <span>{`${row.original.tenant.firstName} ${row.original.tenant.surname}`}</span>
            ),
        },
        {
            accessorKey: "property",
            header: "Property",
            cell: ({ row }: { row: { original: Contract } }) => (
                <span>{row.original.property.name}</span>
            ),
        },
        {
            id: "actions",
            header: "Actions",
            cell: ({ row }: { row: { original: Contract } }) => (
                <button
                    onClick={() => openDeleteDialog(row.original)}
                    className="text-red-600 hover:text-red-800"
                >
                    Delete
                </button>
            ),
        },
    ];

    return (
        <div className="max-w-5xl mx-auto mt-10 p-6 bg-white shadow-md rounded-lg">
            <h1 className="text-3xl font-bold text-center text-gray-800 mb-6">Contracts List</h1>
            {error && (
                <div className="mb-4 p-3 text-red-800 bg-red-100 border border-red-300 rounded">
                    {error}
                </div>
            )}
            <DataTable columns={columns} data={contracts} />
            <ConfirmDialog
                title="Are you sure?"
                description="This action cannot be undone. It will permanently delete the contract."
                confirmLabel="Delete"
                cancelLabel="Cancel"
                isOpen={isDialogOpen}
                onConfirm={handleDelete}
                onCancel={() => setIsDialogOpen(false)}
            />
        </div>
    );
};

export default ContractList;