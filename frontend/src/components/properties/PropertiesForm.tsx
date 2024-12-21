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
    name: z.string().nonempty("Name is required"),
    type: z.string().nonempty("Type is required"),
    address: z.string().nonempty("Address is required"),
    description: z.string().optional(),
    price: z.number().min(0.1, "Price is required"),
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
        },
    });

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
            //if (values.imageFile) {
            //   formData.append("imageFile", values.imageFile);
            //}

            const url: string = propertyToEdit ? `http://localhost:8080/api/properties/${propertyToEdit.id}` : "http://localhost:8080/api/properties";
            const response = await fetch(url, {
                method: propertyToEdit ? "PUT" : "POST",
                body: formData,
            });

            if (response.ok) {
                const data: Property = await response.json();
                if (propertyToEdit) editProperty(data);
                else addProperty(data);
                setIsOpen(false);
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
                                const value: number | null = e.target.value === "" ? null : parseFloat(e.target.value);
                                field.onChange(value);
                            }} />
                        </FormControl>
                        <FormMessage/>
                    </FormItem>
                )}/>
                <Button type="submit">{propertyToEdit ? "Save Changes" : "Create New Property"}</Button>
            </form>
        </Form>
    );
};

export default PropertiesForm;