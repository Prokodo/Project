"use client"

import {z} from "zod";
import {Property} from "@/types/types";
import {useForm} from "react-hook-form";
import {Input} from "@/components/ui/input";
import {Button} from "@/components/ui/button";
import {zodResolver} from "@hookform/resolvers/zod";
import {useProperties} from "@/components/properties/PropertiesContext";
import {Form, FormControl, FormField, FormItem, FormLabel, FormMessage} from "@/components/ui/form";
import {Dispatch, FC, SetStateAction} from "react";

const formSchema = z.object({
    name: z.string().nonempty("Name is required"),
    type: z.string().nonempty("Type is required"),
    address: z.string().nonempty("Address is required"),
    description: z.string().optional(),
    propertyValue: z.string().nonempty("Value is required"),
});

const PropertiesForm: FC<{ setIsOpen: Dispatch<SetStateAction<boolean>> }> = ({ setIsOpen }) => {
    const {addProperty} = useProperties();
    const form = useForm<z.infer<typeof formSchema>>({
        resolver: zodResolver(formSchema),
        defaultValues: {
            name: "",
            type: "",
            address: "",
            description: "",
            propertyValue: "",
        },
    });

    async function onSubmit(values: z.infer<typeof formSchema>) {
        try {
            const response = await fetch("http://localhost:8080/api/properties", {
                method: "POST", headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify(values),
            });

            if (response.ok) {
                const data: Property = await response.json();
                addProperty(data);
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

                <FormField control={form.control} name="propertyValue" render={({field}) => (
                    <FormItem>
                        <FormLabel>Value</FormLabel>
                        <FormControl>
                            <Input placeholder="Property Value" {...field} />
                        </FormControl>
                        <FormMessage/>
                    </FormItem>
                )}/>
                <Button type="submit">Create new property</Button>
            </form>
        </Form>
    );
};

export default PropertiesForm;



