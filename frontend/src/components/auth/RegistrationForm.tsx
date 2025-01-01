"use client"

import { z } from "zod";
import {FC} from "react";
import {useForm} from "react-hook-form";
import {Input} from "@/components/ui/input";
import {Button} from "@/components/ui/button";
import {zodResolver} from "@hookform/resolvers/zod";
import {Form, FormControl, FormField, FormItem, FormLabel, FormMessage} from "@/components/ui/form";

const registrationSchema = z.object({
    username: z.string().nonempty("Username is required!"),
    password: z.string().min(6, "Password must be at least 6 characters!"),
    role: z.enum(["ADMIN", "TENANT"], { required_error: "Role is required!" }),
    firstName: z.string().nonempty("First name is required!"),
    surname: z.string().nonempty("Second name is required!"),
    email: z.string().email("Invalid email address!"),
    phoneNumber: z.string().regex(/^\d{10}$/, "Phone number must be 10 digits!"),
});

const RegistrationForm: FC = () => {
    const form = useForm<z.infer<typeof registrationSchema>>({
        resolver: zodResolver(registrationSchema),
        defaultValues: {
            username: "",
            password: "",
            role: "TENANT",
            firstName: "",
            surname: "",
            email: "",
            phoneNumber: "",
        },
    });

    async function onSubmit(values: z.infer<typeof registrationSchema>): Promise<void> {
        try {
            const url: string = "http://localhost:8080/api/auth/register";
            const response = await fetch(url, {
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

                <FormField control={form.control} name="firstName" render={({ field }) => (
                    <FormItem>
                        <FormLabel>First Name</FormLabel>
                        <FormControl>
                            <Input placeholder="Enter first name" {...field} />
                        </FormControl>
                        <FormMessage />
                    </FormItem>
                )} />

                <FormField control={form.control} name="surname" render={({ field }) => (
                    <FormItem>
                        <FormLabel>Surname</FormLabel>
                        <FormControl>
                            <Input placeholder="Enter surname" {...field} />
                        </FormControl>
                        <FormMessage />
                    </FormItem>
                )} />

                <FormField control={form.control} name="email" render={({ field }) => (
                    <FormItem>
                        <FormLabel>Email</FormLabel>
                        <FormControl>
                            <Input type="email" placeholder="Enter email" {...field} />
                        </FormControl>
                        <FormMessage />
                    </FormItem>
                )} />

                <FormField
                    control={form.control}
                    name="phoneNumber"
                    render={({ field }) => (
                        <FormItem>
                            <FormLabel>Phone Number</FormLabel>
                            <FormControl>
                                <Input placeholder="Enter phone number" {...field} />
                            </FormControl>
                            <FormMessage />
                        </FormItem>
                    )}
                />

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
