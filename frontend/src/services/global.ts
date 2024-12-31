import {AuthorityResponse, RolesResponse} from "@/types/types";

type validRoles = "ROLE_ADMIN" | "ROLE_USER";

async function getUserRoles(authToken: string | undefined): Promise<RolesResponse | undefined> {
    if (!authToken) {
        return undefined;
    }

    try {
        const url: string = `http://localhost:8080/api/auth/roles`;
        const response = await fetch(url, {
            method: "GET",
            headers: {
                "Content-Type": "application/json",
                Authorization: `Bearer ${authToken}`,
            }, cache: "no-store"
        });

        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }
        return await response.json();
    } catch (error) {
        console.error(error);
        return undefined;
    }
}

async function hasAuthority(authToken: string | undefined, authority: validRoles): Promise<AuthorityResponse | undefined> {
    if (!authToken) {
        return undefined;
    }

    try {
        const url: string = `http://localhost:8080/api/auth/has-authority?authority=${encodeURIComponent(authority)}`;
        const response = await fetch(url, {
            method: "GET",
            headers: {
                "Content-Type": "application/json",
                Authorization: `Bearer ${authToken}`,
            }, cache: "no-store"
        });

        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }
        return await response.json();
    } catch (error) {
        console.error(error);
        return undefined;
    }
}

export type { validRoles };
export { hasAuthority, getUserRoles };