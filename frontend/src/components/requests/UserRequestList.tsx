"use client"

import {Request} from "@/types/types";
import {getCookie} from "@/utils/cookies";
import React, {JSX, useState} from "react";
import ConfirmDialog from "@/components/common/ConfirmDialog";
import {useRequests} from "@/components/requests/RequestsContext";

const UserRequestsList = (): JSX.Element => {
    const { requests, setRequests } = useRequests();

    const [searchTerm, setSearchTerm] = useState<string>("");
    const [isDialogOpen, setIsDialogOpen] = useState<boolean>(false);
    const [selectedRequestId, setSelectedRequestId] = useState<number | null>(null);

    const openDeleteDialog = (requestId: number): void => {
        setSelectedRequestId(requestId);
        setIsDialogOpen(true);
    };

    const handleDelete = async (): Promise<void> => {
        try {
            const url = `http://localhost:8080/api/requests/${selectedRequestId}`;
            const authToken: string = getCookie("authToken") || "";
            const response = await fetch(url, {
                method: "DELETE",
                headers: {
                    Authorization: `Bearer ${authToken}`,
                },
            });

            if (response.ok) {
                setRequests((prevRequests: Request[]): Request[] =>
                    prevRequests.filter((r: Request): boolean => r.id !== selectedRequestId)
                );
                setSelectedRequestId(null);
                setIsDialogOpen(false);
            } else {
                throw new Error(`Failed to delete request: ${response.statusText}`);
            }
        } catch (error) {
            console.error("Error deleting request:", error);
        }
    };

    return (
        <div>
            <div className="mt-8 mx-4 p-6 bg-white shadow-sm rounded-xl border border-gray-200">
                <h1 className="text-3xl font-bold text-center text-gray-800 mb-6">Request list</h1>

                <div className="mb-4">
                    <input type="text" placeholder="Search properties..." value={searchTerm}
                           onChange={(e): void => setSearchTerm(e.target.value)}
                           className="w-full p-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-400"/>
                </div>

                <div className="max-h-[720px] overflow-y-auto space-y-3">
                    {requests.map((request: Request) => (
                        <div key={request.id} className="border border-gray-300 rounded-md shadow-sm overflow-hidden py-3 px-4">
                            <div className="flex justify-between items-center pr-4">
                                <div>
                                    <h2 className="text-xl font-semibold text-gray-700">
                                        {request.property.address} | {request.property.name} (Request date: {request.requestDate})
                                    </h2>

                                    <p className="text-sm text-gray-600">
                                        <strong>Description:</strong><br/>
                                        {request.description}
                                    </p>
                                </div>

                                <div className="flex justify-between align-middle">
                                    <div className="flex items-center mr-8">
                                        <span title={request.status}
                                              className={`w-3 h-3 mr-2 block rounded-full ${
                                                  request.status === "REQUESTED" ? "bg-yellow-500" :
                                                      request.status === "IN_PROGRESS" ? "bg-blue-500" :
                                                          request.status === "COMPLETED" ? "bg-green-500" : "bg-red-500"
                                              }`}/>
                                        <span className="text-gray-700 text-sm">{request.status}</span>
                                    </div>

                                    <button onClick={(): void => openDeleteDialog(request.id)} className="text-red-600 text-sm hover:text-red-800">
                                        Delete
                                    </button>
                                </div>
                            </div>
                        </div>
                    ))}
                </div>
            </div>

            <ConfirmDialog title="Are you sure?" confirmLabel="Delete" cancelLabel="Cancel"
                           description="This action cannot be undone. It will permanently delete the tenant."
                           isOpen={isDialogOpen} onConfirm={handleDelete} onCancel={(): void => {
                                setIsDialogOpen(false);
                                setSelectedRequestId(null);
                           }} />

        </div>
    );
};

export default UserRequestsList;
