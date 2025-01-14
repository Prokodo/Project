import {Request} from "@/types/types";

async function fetchRequests(authToken: string | undefined): Promise<Request[] | undefined> {
    if (!authToken) {
        return [];
    }

    try {
        const response = await fetch("http://localhost:8080/api/requests", {
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

export { fetchRequests };