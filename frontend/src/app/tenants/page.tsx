import {ReactElement} from "react";
import {fetchTenants} from "@/services/tenants";
import {RolesResponse, Tenant} from "@/types/types";
import {getAuthToken} from "@/utils/auth";
import {TenantsProvider} from "@/components/tenants/TenantContext";
import TenantsList from "@/components/tenants/TenantList";
import TenantsPopupForm from "@/components/tenants/TenantsPopupForm";
import {getUserRoles} from "@/services/global";
import {redirect, unauthorized} from "next/navigation";

export default async function PropertiesPage(): Promise<ReactElement> {
    const authToken: string | undefined = await getAuthToken();
    const roles: RolesResponse | undefined = await getUserRoles(authToken);
    if (!roles?.loggedIn) {
        redirect('/login');
    }
    if (!roles.roles.includes("ROLE_ADMIN")) {
        unauthorized();
    }

    const tenants: Tenant[] = await fetchTenants(authToken) || [];
    return (
        <TenantsProvider initialTenants={tenants}>
            <TenantsPopupForm />
            {tenants.length > 0 && <TenantsList />}
        </TenantsProvider>
    );
}