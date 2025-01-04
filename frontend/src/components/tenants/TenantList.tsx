"use client"

import {Tenant} from "@/types/types";
import React, {useState} from "react";
import {getCookie} from "@/utils/cookies";
import {DataTable} from "@/components/common/DataTable";
import {useTenants} from "@/components/tenants/TenantContext";
import ConfirmDialog from "@/components/common/ConfirmDialog";
import TenantsRegistrationForm from "@/components/tenants/TenantsRegistrationForm";

const TenantsList = () => {
    const { tenants, setTenants } = useTenants();

    const [error, setError] = useState<string | null>(null);
    const [isDialogOpen, setIsDialogOpen] = useState<boolean>(false);
    const [tenantToEdit, setTenantToEdit] = useState<Tenant | null>(null);
    const [isEditDialogOpen, setIsEditDialogOpen] = useState<boolean>(false);

    const openEditDialog = (tenant: Tenant): void => {
        setTenantToEdit(tenant);
        setIsEditDialogOpen(true);
    };

    const openDeleteDialog = (tenant: Tenant): void => {
        setTenantToEdit(tenant);
        setIsDialogOpen(true);
    };

    const handleDelete = async (): Promise<void> => {
        if (tenantToEdit) {
            try {
                const url: string = `http://localhost:8080/api/users/tenants/${tenantToEdit.id}`;
                const authToken: string = getCookie("authToken") || "";
                const response = await fetch(url, {
                    method: "DELETE",
                    headers: {
                        Authorization: `Bearer ${authToken}`,
                    },
                    cache: "no-store",
                });

                if (response.ok) {
                    setTenants(tenants.filter(
                        (tenant: Tenant): boolean => tenant.id !== tenantToEdit.id
                    ));
                } else {
                    const errorText = `Failed to delete property: ${response.status} ${response.statusText}`;
                    setError(errorText);
                }
            } catch (error: any) {
                setError(`Error deleting property: ${error.message}`);
            } finally {
                setTenantToEdit(null);
            }
        }
        setIsDialogOpen(false);
    };

    const columns = [
        {
            accessorKey: "username",
            header: "Username",
        },
        {
            accessorKey: "firstName",
            header: "First name",
        },
        {
            accessorKey: "surname",
            header: "Surname",
        },
        {
            accessorKey: "email",
            header: "Email",
        },
        {
            accessorKey: "phoneNumber",
            header: "Phone number",
        },
        {
            accessorKey: "role",
            header: "Role",
        },
        {
            id: "actions",
            header: "Actions",
            cell: ({row}: { row: { original: Tenant } }) => (
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
            <h1 className="text-3xl font-bold text-center text-gray-800 mb-6">Tenants List</h1>
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
                        <TenantsRegistrationForm setIsOpen={setIsEditDialogOpen} tenantToEdit={tenantToEdit || undefined}/>
                    </div>
                </div>
            )}
            <DataTable columns={columns} data={tenants} />
        </div>
    );
};

export default TenantsList;
