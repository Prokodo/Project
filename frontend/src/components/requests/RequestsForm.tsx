"use client"

import { z } from "zod";
import {Property} from "@/types/types";
import {useForm} from "react-hook-form";
import {getCookie} from "@/utils/cookies";
import {zodResolver} from "@hookform/resolvers/zod";
import React, { FC, Dispatch, SetStateAction } from "react";
import {useProperties} from "@/components/properties/PropertiesContext";

const requestSchema = z.object({
    description: z.string().min(10, "Description is required").max(255, "Description is too long"),
    status: z.enum(["REQUESTED", "IN_PROGRESS", "COMPLETED", "REJECTED"]),
    requestDate: z.string().min(1, "Request date is required"),
});

type RequestFormProps = {
    setIsOpen: Dispatch<SetStateAction<boolean>>;
    property: Property;
};

const RequestsForm: FC<RequestFormProps> = ({ setIsOpen, property }) => {
    const { addRequest } = useProperties();
    const form = useForm<z.infer<typeof requestSchema>>({
        resolver: zodResolver(requestSchema),
        defaultValues: {
            description: "",
            status: "REQUESTED",
            requestDate: new Date().toISOString().split("T")[0],
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
                addRequest(property.id, newRequest);
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
                <textarea {...form.register("description")} className="mt-1 block w-full border-gray-300 rounded-md shadow-sm"></textarea>
                {form.formState.errors.description && (
                    <p className="text-red-500 text-sm">
                        {form.formState.errors.description.message}
                    </p>
                )}
            </div>

            <input type="hidden" value={"REQUESTED"} {...form.register("status")} />
            <input type="hidden" value={new Date().toISOString().split("T")[0]} {...form.register("requestDate")} />

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
