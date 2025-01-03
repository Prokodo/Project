import {ReactElement} from "react";
import {fetchTenants} from "@/services/tenants";
import {AuthorityResponse, Tenant} from "@/types/types";
import {checkUserAuthority, getAuthToken} from "@/utils/auth";
import {TenantsProvider} from "@/components/tenants/TenantContext";
import TenantsList from "@/components/tenants/TenantList";
import TenantsPopupForm from "@/components/tenants/TenantsPopupForm";

export default async function PropertiesPage(): Promise<ReactElement> {
    const authToken: string | undefined = await getAuthToken();
    await checkUserAuthority(authToken, "ROLE_ADMIN");

    const tenants: Tenant[] = await fetchTenants(authToken) || [];
    return (
        <TenantsProvider initialTenants={tenants}>
            <TenantsPopupForm />
            {tenants.length > 0 && <TenantsList />}
        </TenantsProvider>
    );
}