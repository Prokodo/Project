import {cookies} from "next/headers";
import {AuthorityResponse} from "@/types/types";
import {redirect, unauthorized} from "next/navigation";
import {hasAuthority, validRoles} from "@/services/global";
import {ReadonlyRequestCookies} from "next/dist/server/web/spec-extension/adapters/request-cookies";

async function getAuthToken(): Promise<string | undefined> {
    const cookieStore: ReadonlyRequestCookies = await cookies();
    return cookieStore.get("authToken")?.value;
}

async function checkUserAuthority(authToken: string | undefined, requiredRole: validRoles): Promise<AuthorityResponse | never> {
    if (!authToken) {
        redirect('/login');
    }

    const authority: AuthorityResponse | undefined = await hasAuthority(authToken, requiredRole);
    if (!authority || !authority.authorized) {
        return unauthorized();
    }
    return authority;
}

export {getAuthToken, checkUserAuthority};