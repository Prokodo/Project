"use client"

import { z } from "zod";
import React, {Dispatch, FC, SetStateAction} from "react";
import {useForm} from "react-hook-form";
import {Input} from "@/components/ui/input";
import {Button} from "@/components/ui/button";
import {zodResolver} from "@hookform/resolvers/zod";
import {Form, FormControl, FormField, FormItem, FormLabel, FormMessage} from "@/components/ui/form";
import {Tenant} from "@/types/types";
import {useTenants} from "@/components/tenants/TenantContext";
import {getCookie} from "@/utils/cookies";
import {toast} from "sonner";

const registrationSchema = z.object({
    username: z.string().nonempty("Username is required!"),
    password: z.string().min(6, "Password must be at least 6 characters!"),
    role: z.enum(["MANAGER", "TENANT"], { required_error: "Role is required!" }),
    firstName: z.string().nonempty("First name is required!"),
    surname: z.string().nonempty("Second name is required!"),
    email: z.string().email("Invalid email address!"),
    phoneNumber: z.string().regex(/^\+420(\s?\d){9}$|^(\s?\d){9}$/, "Phone number must be in the format '+420123456789' or '123456789'"),
});

const TenantsRegistrationForm: FC<{ setIsOpen: Dispatch<SetStateAction<boolean>>, tenantToEdit?: Tenant, isAdmin: boolean }> = ({ setIsOpen, tenantToEdit, isAdmin }) => {
    const { addTenant, editTenant } = useTenants();
    const form = useForm<z.infer<typeof registrationSchema>>({
        resolver: zodResolver(registrationSchema),
        defaultValues: {
            username: tenantToEdit?.username || "",
            password: "",
            role: tenantToEdit?.role || "TENANT",
            firstName: tenantToEdit?.firstName || "",
            surname: tenantToEdit?.surname || "",
            email: tenantToEdit?.email || "",
            phoneNumber: tenantToEdit?.phoneNumber || "",
        },
    });

    async function onSubmit(values: z.infer<typeof registrationSchema>): Promise<void> {
        try {
            const url: string = tenantToEdit ? `http://localhost:8080/api/users/tenants/${tenantToEdit.id}` : "http://localhost:8080/api/users/tenants/register";
            const authToken: string = getCookie("authToken") || "";
            const response = await fetch(url, {
                method: tenantToEdit ? "PUT" : "POST",
                headers: {
                    "Content-Type": "application/json",
                    Authorization: `Bearer ${authToken}`,
                },
                body: JSON.stringify(values),
            });

            if (response.ok) {
                const data: Tenant = await response.json();
                if (tenantToEdit) editTenant(data);
                else addTenant(data);
                setIsOpen(false);

                toast(tenantToEdit? "User has been successfully updated." : "New user has been successfully created.");
            } else if (response.status === 400) {
                const errorData = await response.json();
                for (const [field, message] of Object.entries(errorData)) {
                    form.setError(field as keyof z.infer<typeof registrationSchema>, {
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
                <FormField control={form.control} name="username" render={({field}) => (
                    <FormItem>
                        <FormLabel>Username</FormLabel>
                        <FormControl>
                            <Input placeholder="Enter username" {...field} />
                        </FormControl>
                        <FormMessage/>
                    </FormItem>
                )}/>

                <FormField control={form.control} name="password" render={({field}) => (
                    <FormItem>
                        <FormLabel>Password</FormLabel>
                        <FormControl>
                            <Input type="password" placeholder="Enter password" {...field} />
                        </FormControl>
                        <FormMessage/>
                    </FormItem>
                )}/>

                <FormField control={form.control} name="firstName" render={({field}) => (
                    <FormItem>
                        <FormLabel>First Name</FormLabel>
                        <FormControl>
                            <Input placeholder="Enter first name" {...field} />
                        </FormControl>
                        <FormMessage/>
                    </FormItem>
                )}/>

                <FormField control={form.control} name="surname" render={({field}) => (
                    <FormItem>
                        <FormLabel>Surname</FormLabel>
                        <FormControl>
                            <Input placeholder="Enter surname" {...field} />
                        </FormControl>
                        <FormMessage/>
                    </FormItem>
                )}/>

                <FormField control={form.control} name="email" render={({field}) => (
                    <FormItem>
                        <FormLabel>Email</FormLabel>
                        <FormControl>
                            <Input type="email" placeholder="Enter email" {...field} />
                        </FormControl>
                        <FormMessage/>
                    </FormItem>
                )}/>

                <FormField control={form.control} name="phoneNumber" render={({field}) => (
                    <FormItem>
                        <FormLabel>Phone Number</FormLabel>
                        <FormControl>
                            <Input placeholder="Enter phone number" {...field} />
                        </FormControl>
                        <FormMessage/>
                    </FormItem>
                )} />

                {isAdmin ?
                    <FormField control={form.control} name="role" render={({field}) => (
                        <FormItem>
                            <FormLabel>User role </FormLabel>
                            <FormControl>
                                <select {...field} disabled={!isAdmin} className="border ml-3 rounded p-2">
                                    <option value="TENANT">Tenant</option>
                                    <option value="MANAGER">Manager</option>
                                </select>
                            </FormControl>
                            <FormMessage/>
                        </FormItem>
                    )}/>
                    :
                    <input type="hidden" value="TENANT" {...form.register("role")} />
                }
                <Button type="submit">{tenantToEdit? "Save changes" : "Register new user"}</Button>
            </form>
        </Form>
    );
};

export default TenantsRegistrationForm;
