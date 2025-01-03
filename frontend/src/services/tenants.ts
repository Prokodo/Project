import {Tenant} from "@/types/types";

async function fetchTenants(authToken: string | undefined): Promise<Tenant[] | undefined> {
    if (!authToken) {
        return [];
    }

    try {
        const url: string = "http://localhost:8080/api/users/tenants";
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

export { fetchTenants };