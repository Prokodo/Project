"use client"

import React, {useState} from "react";
import {Property} from "@/types/types";
import {getCookie} from "@/utils/cookies";
import {DataTable} from "@/components/common/DataTable";
import ConfirmDialog from "@/components/common/ConfirmDialog";
import PropertiesForm from "@/components/properties/PropertiesForm";
import {useProperties} from "@/components/properties/PropertiesContext";

const PropertiesList = () => {
    const { properties, setProperties } = useProperties();

    const [error, setError] = useState<string | null>(null);
    const [isDialogOpen, setIsDialogOpen] = useState<boolean>(false);
    const [isEditDialogOpen, setIsEditDialogOpen] = useState<boolean>(false);
    const [propertyToEdit, setPropertyToEdit] = useState<Property | null>(null);

    const handleDelete = async (): Promise<void> => {
        if (propertyToEdit) {
            try {
                const url: string = `http://localhost:8080/api/properties/${propertyToEdit.id}`;
                const authToken: string = getCookie("authToken") || "";
                const response = await fetch(url, {
                    method: "DELETE",
                    headers: {
                        Authorization: `Bearer ${authToken}`,
                    },
                    cache: "no-store",
                });

                if (response.ok) {
                    const updatedProperties: Property[] = properties.filter(
                        (property: Property): boolean => property.id !== propertyToEdit.id
                    );
                    setProperties(updatedProperties);
                } else {
                    const errorText = `Failed to delete property: ${response.status} ${response.statusText}`;
                    setError(errorText);
                }
            } catch (error: any) {
                setError(`Error deleting property: ${error.message}`);
            } finally {
                setPropertyToEdit(null);
            }
        }
        setIsDialogOpen(false);
    };

    const openDeleteDialog = (property: Property): void => {
        setPropertyToEdit(property);
        setIsDialogOpen(true);
    };

    const openEditDialog = (property: Property): void => {
        setPropertyToEdit(property);
        setIsEditDialogOpen(true);
    };

    const columns = [
        {
            accessorKey: "imageBytes",
            header: "Image",
            cell: ({ row }: { row: { original: Property } }) => {
                if (!row.original.image) {
                    return (
                      <div>No image found</div>
                    );
                }

                return (
                    <img className="w-20 h-20 object-cover rounded-md"
                         alt={row.original.name} title={row.original.name}
                         src={`data:image/jpeg;base64,${row.original.image}`} />
                );
            },
        },
        {
            accessorKey: "name",
            header: "Name",
        },
        {
            accessorKey: "address",
            header: "Address",
        },
        {
            accessorKey: "type",
            header: "Type",
        },
        {
            accessorKey: "description",
            header: "Description",
        },
        {
            accessorKey: "price",
            header: "Price per month",
            cell: ({ row } : { row: { original: { price: number } } }): string => {
                const price: number = row.original.price;
                return `${price} kč`;
            }
        },
        {
            id: "actions",
            header: "Actions",
            cell: ({ row }: { row: { original: Property } }) => (
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
                            <button onClick={(): void => openDeleteDialog(row.original)}
                                    className="text-red-600 hover:text-red-800">
                                Delete
                            </button>
                        </div>
                    }
                />
            ),
        },
    ];

    return (
        <div className="max-w-5xl mx-auto mt-10 p-6 bg-white shadow-md rounded-lg">
            <h1 className="text-3xl font-bold text-center text-gray-800 mb-6">Properties List</h1>
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
                        <PropertiesForm setIsOpen={setIsEditDialogOpen} propertyToEdit={propertyToEdit || undefined}/>
                    </div>
                </div>
            )}
            <DataTable columns={columns} data={properties} />
        </div>
    );
};

export default PropertiesList;
