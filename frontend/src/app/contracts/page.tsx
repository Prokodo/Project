import {ReactElement} from "react";
import {fetchTenants} from "@/services/tenants";
import {fetchContracts} from "@/services/contracts";
import {fetchProperties} from "@/services/properties";
import {Contract, Property, Tenant} from "@/types/types";
import {checkUserAuthority, getAuthToken} from "@/utils/auth";
import ContractList from "@/components/contracts/ContractList";
import {TenantsProvider} from "@/components/tenants/TenantContext";
import {ContractProvider} from "@/components/contracts/ContractContext";
import ContractPopupForm from "@/components/contracts/ContractPopupForm";
import {PropertiesProvider} from "@/components/properties/PropertiesContext";

export default async function PropertiesPage(): Promise<ReactElement> {
    const authToken: string | undefined = await getAuthToken();
    await checkUserAuthority(authToken, "ROLE_ADMIN");

    const [tenants, contracts, properties] = await Promise.all([
        fetchTenants(authToken).catch((): Tenant[] => [] as Tenant[]),
        fetchContracts(authToken).catch((): Contract[] => [] as Contract[]),
        fetchProperties(authToken).catch((): Property[] => [] as Property[])
    ]);

    return (
        <PropertiesProvider initialProperties={properties || []}>
            <TenantsProvider initialTenants={tenants || []}>
                <ContractProvider initialContracts={contracts || []}>
                    <ContractPopupForm />
                    <ContractList />
                </ContractProvider>
            </TenantsProvider>
        </PropertiesProvider>
    );
}