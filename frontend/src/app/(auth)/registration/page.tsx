import {ReactElement} from "react";
import {cookies} from "next/headers";
import {hasAuthority} from "@/services/global";
import {AuthorityResponse} from "@/types/types";
import {redirect, unauthorized} from "next/navigation";
import RegistrationForm from "@/components/auth/RegistrationForm";
import {ReadonlyRequestCookies} from "next/dist/server/web/spec-extension/adapters/request-cookies";

export default async function PropertiesPage(): Promise<ReactElement> {
    const cookieStore: ReadonlyRequestCookies = await cookies();
    const authToken: string | undefined = cookieStore.get("authToken")?.value;
    if (!authToken) {
        redirect('/login');
    }

    const authority: AuthorityResponse | undefined = await hasAuthority(authToken, "ROLE_USER");
    if (!authority) {
        return unauthorized();
    }

    return (
        <RegistrationForm />
    );
}