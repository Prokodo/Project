"use client"

import {Tenant} from "@/types/types";
import React, {useMemo, useState} from "react";
import {getCookie} from "@/utils/cookies";
import {DataTable} from "@/components/common/DataTable";
import {useTenants} from "@/components/tenants/TenantContext";
import ConfirmDialog from "@/components/common/ConfirmDialog";
import TenantsRegistrationForm from "@/components/tenants/TenantsRegistrationForm";
import {toast} from "sonner";

const TenantsList = ({ isAdmin }: { isAdmin: boolean }) => {
    const { tenants, setTenants } = useTenants();

    const [error, setError] = useState<string | null>(null);
    const [searchTerm, setSearchTerm] = useState<string>("");
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
                    toast("Tenant has been successfully deleted.");
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

    const filteredTenants: Tenant[] = useMemo((): Tenant[] => {
        const searchWords: string[] = searchTerm.toLowerCase().split(" ").filter((word: string): boolean => word.trim() !== "");
        return tenants.filter((tenant: Tenant): boolean => {
            return searchWords.every((word: string): boolean => {
                return (
                    tenant.username.toLowerCase().includes(word) ||
                    tenant.firstName.toLowerCase().includes(word) ||
                    tenant.surname.toLowerCase().includes(word) ||
                    tenant.email.toLowerCase().includes(word) ||
                    tenant.phoneNumber.toLowerCase().includes(word)
                );
            });
        });
    }, [tenants, searchTerm]);

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
                <div className="flex space-x-4">
                    <button onClick={(): void => openEditDialog(row.original)} className="text-blue-600 hover:text-blue-800">
                        Edit
                    </button>
                    <button onClick={(): void => openDeleteDialog(row.original)} className="text-red-600 hover:text-red-800">
                        Delete
                    </button>
                </div>
            ),
        }
    ];

    return (
        <div className="mt-8 mx-4 p-6 bg-white shadow-sm rounded-xl border border-gray-200">
            <h1 className="text-3xl font-bold text-center text-gray-800 mb-6">
                {isAdmin ? "User list" : "Tenant list"}
            </h1>

            {error && (
                <div className="mb-4 p-3 text-red-800 bg-red-100 border border-red-300 rounded">
                    {error}
                </div>
            )}

            <div className="mb-4">
                <input type="text" placeholder="Search tenants..." value={searchTerm}
                       onChange={(e): void => setSearchTerm(e.target.value)}
                       className="w-full p-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-400"/>
            </div>

            <ConfirmDialog title="Are you sure?"
                           confirmLabel="Delete"
                           cancelLabel="Cancel"
                           description="This action cannot be undone. It will permanently delete the tenant."
                           isOpen={isDialogOpen}
                           onConfirm={handleDelete}
                           onCancel={(): void => setIsDialogOpen(false)}/>

            {isEditDialogOpen && (
                <div className="fixed inset-0 bg-gray-900 bg-opacity-50 flex items-center justify-center z-50">
                    <div className="bg-white rounded-lg shadow-lg w-full max-w-2xl p-6 relative">
                        <button onClick={() => setIsEditDialogOpen(false)}
                                className="absolute top-3 right-3 text-gray-500 hover:text-gray-700 focus:outline-none">
                            ✖
                        </button>
                        <h2 className="text-2xl font-bold text-center mb-6">Edit user</h2>
                        <TenantsRegistrationForm setIsOpen={setIsEditDialogOpen} isAdmin={false}
                                                 tenantToEdit={tenantToEdit || undefined}/>
                    </div>
                </div>
            )}

            <div className="max-h-[710px] overflow-y-auto">
                <DataTable columns={columns} data={filteredTenants}/>
            </div>
        </div>
    );
};

export default TenantsList;
