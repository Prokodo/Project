import {Contract} from "@/types/types";

async function fetchContracts(authToken: string | undefined): Promise<Contract[] | undefined> {
    if (!authToken) {
        return [];
    }

    try {
        const url: string = "http://localhost:8080/api/contracts";
        const response = await fetch(url, {
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

export { fetchContracts };