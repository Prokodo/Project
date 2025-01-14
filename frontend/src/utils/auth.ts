import {cookies} from "next/headers";
import {ReadonlyRequestCookies} from "next/dist/server/web/spec-extension/adapters/request-cookies";

async function getAuthToken(): Promise<string | undefined> {
    const cookieStore: ReadonlyRequestCookies = await cookies();
    return cookieStore.get("authToken")?.value;
}

interface CSRFToken {
    parameterName: string,
    headerName: string,
    token: string,
}

async function getCsrfToken(): Promise<CSRFToken> {
    const response = await fetch('http://localhost:8080/api/auth/csrf-token', {
        credentials: 'include',
    });
    if (!response.ok) {
        throw new Error(response.statusText || 'Failed to fetch CSRF token');
    }
    return await response.json();
}

export type { CSRFToken }
export {getAuthToken, getCsrfToken};