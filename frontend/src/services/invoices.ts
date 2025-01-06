import {Invoice} from "@/types/types";

async function fetchInvoices(authToken: string | undefined): Promise<Invoice[] | undefined> {
    if (!authToken) {
        return [];
    }

    try {
        const response = await fetch("http://localhost:8080/api/invoices", {
            method: "GET", headers: {
                "Content-Type": "application/json",
                Authorization: `Bearer ${authToken}`,
            }, cache: "no-store",
        });

        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }
        return await response.json();
    } catch (error) {
        console.error(error);
    }
}

export { fetchInvoices };