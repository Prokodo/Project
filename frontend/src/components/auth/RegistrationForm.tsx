"use client"

import { z } from "zod";
import {useForm} from "react-hook-form";
import {Input} from "@/components/ui/input";
import {Button} from "@/components/ui/button";
import {Dispatch, FC, SetStateAction} from "react";
import {zodResolver} from "@hookform/resolvers/zod";
import {Form, FormControl, FormField, FormItem, FormLabel, FormMessage} from "@/components/ui/form";

const registrationSchema = z.object({
    username: z.string().nonempty("Username is required!"),
    password: z.string().min(6, "Password must be at least 6 characters!"),
    role: z.enum(["ADMIN", "TENANT"], { required_error: "Role is required!" }),
});

const RegistrationForm: FC = () => {
    const form = useForm<z.infer<typeof registrationSchema>>({
        resolver: zodResolver(registrationSchema),
        defaultValues: {
            username: "",
            password: "",
            role: "TENANT", // Default role
        },
    });

    async function onSubmit(values: z.infer<typeof registrationSchema>): Promise<void> {
        try {
            const response = await fetch("http://localhost:8080/api/auth/register", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify(values),
            });

            if (response.ok) {
                alert("User registered successfully!");
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
                <FormField control={form.control} name="username" render={({ field }) => (
                    <FormItem>
                        <FormLabel>Username</FormLabel>
                        <FormControl>
                            <Input placeholder="Enter username" {...field} />
                        </FormControl>
                        <FormMessage />
                    </FormItem>
                )} />

                <FormField control={form.control} name="password" render={({ field }) => (
                    <FormItem>
                        <FormLabel>Password</FormLabel>
                        <FormControl>
                            <Input type="password" placeholder="Enter password" {...field} />
                        </FormControl>
                        <FormMessage />
                    </FormItem>
                )} />

                <FormField control={form.control} name="role" render={({ field }) => (
                    <FormItem>
                        <FormLabel>Role</FormLabel>
                        <FormControl>
                            <select {...field} className="form-select">
                                <option value="ADMIN">Admin</option>
                                <option value="TENANT">Tenant</option>
                            </select>
                        </FormControl>
                        <FormMessage />
                    </FormItem>
                )} />

                <Button type="submit">Register</Button>
            </form>
        </Form>
    );
};

export default RegistrationForm;
