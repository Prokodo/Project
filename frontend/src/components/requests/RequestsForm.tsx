"use client"

import { z } from "zod";
import {Property} from "@/types/types";
import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import React, { FC, Dispatch, SetStateAction } from "react";
import {useProperties} from "@/components/properties/PropertiesContext";
import {getCookie} from "@/utils/cookies";

const requestSchema = z.object({
    description: z.string().min(1, "Description is required"),
    status: z.enum(["REQUESTED", "IN_PROGRESS", "COMPLETED", "REJECTED"]),
    requestDate: z.string().min(1, "Request date is required"),
    completionDate: z.string().optional(),
});

type RequestFormProps = {
    setIsOpen: Dispatch<SetStateAction<boolean>>;
    property: Property;
};

const RequestsForm: FC<RequestFormProps> = ({ setIsOpen, property }) => {
    const {  } = useProperties();
    const form = useForm<z.infer<typeof requestSchema>>({
        resolver: zodResolver(requestSchema),
        defaultValues: {
            description: "",
            status: "REQUESTED",
            requestDate: new Date().toISOString().split("T")[0],
            completionDate: undefined,
        },
    });

    const onSubmit = async (values: z.infer<typeof requestSchema>) => {
        try {
            const url: string = `http://localhost:8080/api/requests/${property.id}`;
            const authToken: string = getCookie("authToken") || "";
            const response = await fetch(url, {
                method: "POST",
                body: JSON.stringify(values),
                headers: {
                    "Content-Type": "application/json",
                    Authorization: `Bearer ${authToken}`,
                },
            });

            if (response.ok) {
                const newRequest = await response.json();
                //addRequest(property.id, newRequest);
                setIsOpen(false);
            } else {
                alert("Failed to add request.");
            }
        } catch (error) {
            console.error("Error adding request:", error);
        }
    };

    return (
        <form onSubmit={form.handleSubmit(onSubmit)} className="space-y-4">
            <div>
                <label className="block text-sm font-medium text-gray-700">Description</label>
                <textarea{...form.register("description")}
                         className="mt-1 block w-full border-gray-300 rounded-md shadow-sm"></textarea>
                {form.formState.errors.description && (
                    <p className="text-red-500 text-sm">
                        {form.formState.errors.description.message}
                    </p>
                )}
            </div>

            <div>
                <label className="block text-sm font-medium text-gray-700">Status</label>
                <select {...form.register("status")} className="mt-1 block w-full border-gray-300 rounded-md shadow-sm">
                    <option value="REQUESTED">Requested</option>
                    <option value="IN_PROGRESS">In Progress</option>
                    <option value="COMPLETED">Completed</option>
                    <option value="REJECTED">Rejected</option>
                </select>
                {form.formState.errors.status && (
                    <p className="text-red-500 text-sm">
                        {form.formState.errors.status.message}
                    </p>
                )}
            </div>

            <div>
                <label className="block text-sm font-medium text-gray-700">Request Date</label>
                <input type="date" readOnly value={new Date().toISOString().split("T")[0]}
                       className="mt-1 block w-full border-gray-300 rounded-md shadow-sm bg-gray-100 cursor-not-allowed"/>
                {form.formState.errors.requestDate && (
                    <p className="text-red-500 text-sm">
                        {form.formState.errors.requestDate.message}
                    </p>
                )}
            </div>

            <div>
                <label className="block text-sm font-medium text-gray-700">Completion Date</label>
                <input type="date"{...form.register("completionDate")}
                       className="mt-1 block w-full border-gray-300 rounded-md shadow-sm"/>
                {form.formState.errors.completionDate && (
                    <p className="text-red-500 text-sm">
                        {form.formState.errors.completionDate.message}
                    </p>
                )}
            </div>

            <div className="flex justify-end space-x-4">
                <button type="button" className="bg-gray-300 px-4 py-2 rounded" onClick={() => setIsOpen(false)}>
                    Cancel
                </button>

                <button type="submit" className="bg-blue-500 text-white px-4 py-2 rounded">
                    Add Request
                </button>
            </div>
        </form>
    );
};

export default RequestsForm;
