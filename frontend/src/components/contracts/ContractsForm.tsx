"use client";

import { z } from "zod";
import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { Input } from "@/components/ui/input";
import { Button } from "@/components/ui/button";
import { Form, FormControl, FormField, FormItem, FormLabel, FormMessage } from "@/components/ui/form";
import { FC, useState } from "react";
import {Property, Tenant} from "@/types/types";
import {useProperties} from "@/components/properties/PropertiesContext";
import {useTenants} from "@/components/tenants/TenantContext";
import {getCookie} from "@/utils/cookies";

const contractSchema = z.object({
    propertyId: z.string().nonempty("Property is required!"),
    tenantId: z.string().nonempty("Tenant is required!"),
    startDate: z.string().nonempty("Start date is required!"),
    endDate: z.string().nonempty("End date is required!"),
    monthlyRent: z.coerce.number().min(1, "Monthly rent must be greater than 0!"),
});

type ContractFormValues = z.infer<typeof contractSchema>;

const ContractForm: FC = () => {
    const [loading, setLoading] = useState(false);
    const tenants: Tenant[] = useTenants().tenants;
    const properties: Property[] = useProperties().properties;

    const form = useForm<ContractFormValues>({
        resolver: zodResolver(contractSchema),
        defaultValues: {
            propertyId: "",
            tenantId: "",
            startDate: "",
            endDate: "",
            monthlyRent: 0,
        },
    });

    async function onSubmit(values: z.infer<typeof contractSchema>): Promise<void> {
        try {
            const url: string = "http://localhost:8080/api/contracts";
            const authToken: string = getCookie("authToken") || "";
            const response = await fetch(url, {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                    Authorization: `Bearer ${authToken}`,
                },
                body: JSON.stringify(values),
            });

            if (response.ok) {
                const data: Tenant = await response.json();

            } else if (response.status === 400) {
                const errorData = await response.json();
                for (const [field, message] of Object.entries(errorData)) {
                    form.setError(field as keyof z.infer<typeof contractSchema>, {
                        type: "manual",
                        message: message as string,
                    });
                }
            } else {
                alert("Failed to register user.");
                console.error("Error:", response.statusText);
            }
        } catch (error) {
            alert("An error occurred while registering the user.");
            console.error("Error:", error);
        }
    }

    return (
        <Form {...form}>
            <form onSubmit={form.handleSubmit(onSubmit)} className="space-y-4">
                <FormField control={form.control} name="propertyId" render={({ field }) => (
                    <FormItem>
                        <FormLabel>Property</FormLabel>
                        <FormControl>
                            <select {...field} value={field.value || ""} className="form-select">
                                <option value="" disabled>Select a property</option>
                                {properties.map((property: Property) => (
                                    <option key={property.id} value={property.id}>
                                        {property.name}
                                    </option>
                                ))}
                            </select>
                        </FormControl>
                        <FormMessage />
                    </FormItem>
                )} />

                <FormField control={form.control} name="tenantId" render={({ field }) => (
                    <FormItem>
                        <FormLabel>Tenant</FormLabel>
                        <FormControl>
                            <select {...field} className="form-select">
                                <option value="" disabled>Select a tenant</option>
                                {tenants.map((tenant) => (
                                    <option key={tenant.id} value={tenant.id}>
                                        {tenant.username}
                                    </option>
                                ))}
                            </select>
                        </FormControl>
                        <FormMessage />
                    </FormItem>
                )} />

                <FormField control={form.control} name="startDate" render={({ field }) => (
                    <FormItem>
                        <FormLabel>Start Date</FormLabel>
                        <FormControl>
                            <Input type="date" {...field} />
                        </FormControl>
                        <FormMessage />
                    </FormItem>
                )} />

                <FormField control={form.control} name="endDate" render={({ field }) => (
                    <FormItem>
                        <FormLabel>End Date</FormLabel>
                        <FormControl>
                            <Input type="date" {...field} />
                        </FormControl>
                        <FormMessage />
                    </FormItem>
                )} />

                <FormField control={form.control} name="monthlyRent" render={({ field }) => (
                    <FormItem>
                        <FormLabel>Monthly Rent (Kč)</FormLabel>
                        <FormControl>
                            <Input type="number" min="0" step="0.01" placeholder="Enter monthly rent" {...field} />
                        </FormControl>
                        <FormMessage />
                    </FormItem>
                )} />

                <Button type="submit" disabled={loading}>
                    {loading ? "Submitting..." : "Submit"}
                </Button>
            </form>
        </Form>
    );
};

export default ContractForm;
