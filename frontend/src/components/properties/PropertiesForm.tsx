"use client"

import {z} from "zod";
import {Property} from "@/types/types";
import {useForm} from "react-hook-form";
import {Input} from "@/components/ui/input";
import {Button} from "@/components/ui/button";
import {Dispatch, FC, SetStateAction} from "react";
import {zodResolver} from "@hookform/resolvers/zod";
import {useProperties} from "@/components/properties/PropertiesContext";
import {Form, FormControl, FormField, FormItem, FormLabel, FormMessage} from "@/components/ui/form";

const formSchema = z.object({
    name: z.string().nonempty("Name is required!"),
    type: z.string().nonempty("Type is required!"),
    address: z.string().nonempty("Address is required!"),
    description: z.string().optional(),
    price: z.number().min(0.1, "Price is required!"),
    imageFile: z.any().optional(),
});

const PropertiesForm: FC<{ setIsOpen: Dispatch<SetStateAction<boolean>>, propertyToEdit?: Property }> = ({ setIsOpen, propertyToEdit }) => {
    const { addProperty, editProperty } = useProperties();
    const form = useForm<z.infer<typeof formSchema>>({
        resolver: zodResolver(formSchema),
        defaultValues: {
            name: propertyToEdit?.name || "",
            type: propertyToEdit?.type || "",
            address: propertyToEdit?.address || "",
            description: propertyToEdit?.description || "",
            price: propertyToEdit?.price || 0,
            imageFile: null,
        },
    });

    const getCookie = (name) => {
        const value = `; ${document.cookie}`;
        const parts = value.split(`; ${name}=`);
        if (parts.length === 2) return parts.pop().split(';').shift();
        return null;
    };


    async function onSubmit(values: z.infer<typeof formSchema>) {
        try {
            const formData = new FormData();
            const propertyBlob = new Blob([
                JSON.stringify({
                    name: values.name,
                    type: values.type,
                    price: values.price,
                    address: values.address,
                    description: values.description,
                }),
            ], { type: "application/json" });
            formData.append("property", propertyBlob);

            if (values.imageFile && values.imageFile.length > 0) {
                formData.append("imageFile", values.imageFile[0]);
            } else if (propertyToEdit?.image) {
                const imageBlob = new Blob([
                    new Uint8Array(Array.from(atob(propertyToEdit.image), char => char.charCodeAt(0)))
                ], { type: "image/*" });
                formData.append("imageFile", imageBlob);
            }

            const authToken = getCookie("authToken");

            const url: string = propertyToEdit ? `http://localhost:8080/api/properties/${propertyToEdit.id}` : "http://localhost:8080/api/properties";
            const response = await fetch(url, {
                method: propertyToEdit ? "PUT" : "POST",
                body: formData,
                headers: {
                    Authorization: `Bearer ${authToken}`,
                },
                cache: "no-store",
            });

            if (response.ok) {
                const data: Property = await response.json();
                if (propertyToEdit) editProperty(data);
                else addProperty(data);
                setIsOpen(false);
            } else if (response.status === 400) {
                const errorData = await response.json();
                for (const [field, message] of Object.entries(errorData)) {
                    form.setError(field as keyof z.infer<typeof formSchema>, {
                        type: "manual",
                        message: message as string,
                    });
                }
            } else {
                alert("Failed to create property.");
                console.error("Error:", response.statusText);
            }
        } catch (error) {
            alert("An error occurred while creating the property.");
            console.error("Error:", error);
        }
    }

    return (
        <Form {...form}>
            <form onSubmit={form.handleSubmit(onSubmit)} className="space-y-4">
                <FormField control={form.control} name="name" render={({field}) => (
                    <FormItem>
                        <FormLabel>Name</FormLabel>
                        <FormControl>
                            <Input placeholder="Property Name" {...field} />
                        </FormControl>
                        <FormMessage/>
                    </FormItem>
                )}/>

                <FormField control={form.control} name="type" render={({field}) => (
                    <FormItem>
                        <FormLabel>Type</FormLabel>
                        <FormControl>
                            <Input placeholder="Property Type" {...field} />
                        </FormControl>
                        <FormMessage/>
                    </FormItem>
                )}/>

                <FormField control={form.control} name="address" render={({field}) => (
                    <FormItem>
                        <FormLabel>Address</FormLabel>
                        <FormControl>
                            <Input placeholder="Property Address" {...field} />
                        </FormControl>
                        <FormMessage/>
                    </FormItem>
                )}/>

                <FormField control={form.control} name="description" render={({field}) => (
                    <FormItem>
                        <FormLabel>Description</FormLabel>
                        <FormControl>
                            <Input placeholder="Property Description" {...field} />
                        </FormControl>
                        <FormMessage/>
                    </FormItem>
                )}/>

                <FormField control={form.control} name="price" render={({field}) => (
                    <FormItem>
                        <FormLabel>Price</FormLabel>
                        <FormControl>
                            <Input type="number" placeholder="Property price" {...field} onChange={(e): void => {
                                const value: string | number | null = e.target.value === "" ? "" : parseFloat(e.target.value);
                                field.onChange(value);
                            }} />
                        </FormControl>
                        <FormMessage/>
                    </FormItem>
                )}/>

                <FormField control={form.control} name="imageFile" render={({ field }) => (
                    <FormItem>
                        <FormLabel>Image</FormLabel>
                        <FormControl>
                            <Input type="file" accept="image/*" onChange={(e) => field.onChange(e.target.files)} />
                        </FormControl>
                        <FormMessage />
                    </FormItem>
                )} />

                <Button type="submit">{propertyToEdit ? "Save Changes" : "Create New Property"}</Button>
            </form>
        </Form>
    );
};

export default PropertiesForm;