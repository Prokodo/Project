import {ReactElement} from "react";
import {fetchTenants} from "@/services/tenants";
import {fetchContracts} from "@/services/contracts";
import {fetchProperties} from "@/services/properties";
import {Contract, Property, RolesResponse, Tenant} from "@/types/types";
import {getAuthToken} from "@/utils/auth";
import ContractList from "@/components/contracts/ContractList";
import {TenantsProvider} from "@/components/tenants/TenantContext";
import {ContractProvider} from "@/components/contracts/ContractContext";
import ContractPopupForm from "@/components/contracts/ContractPopupForm";
import {PropertiesProvider} from "@/components/properties/PropertiesContext";
import {getUserRoles} from "@/services/global";
import {redirect} from "next/navigation";
import {HelpCircleIcon} from "lucide-react";
import {Metadata} from "next";

export async function generateMetadata(): Promise<Metadata> {
    return {
        title: 'TenantFlow | Login',
    };
}

export default async function PropertiesPage(): Promise<ReactElement> {
    const authToken: string | undefined = await getAuthToken();
    const roles: RolesResponse | undefined = await getUserRoles(authToken);
    if (!roles?.loggedIn) {
        redirect('/login');
    }

    const isAdmin: boolean = roles.roles.includes("ROLE_ADMIN");
    const fetchData = {
        contracts: (): Promise<Contract[] | undefined> => fetchContracts(authToken),
        tenants: isAdmin ? (): Promise<Tenant[] | undefined> => fetchTenants(authToken) : (): Promise<Tenant[]> => Promise.resolve([] as Tenant[]),
        properties: isAdmin ? (): Promise<Property[] | undefined> => fetchProperties(authToken) : (): Promise<Property[]> => Promise.resolve([] as Property[])
    };

    const [tenants, contracts, properties] = await Promise.all([
        fetchData.tenants().catch((): Tenant[] => [] as Tenant[]),
        fetchData.contracts().catch((): Contract[] => [] as Contract[]),
        fetchData.properties().catch((): Property[] => [] as Property[])
    ]);

    return (
        <main className="min-h-screen bg-gray-50">
            <header className="bg-blue-600 text-white py-4 shadow-md">
                <div className="mx-auto px-10 flex items-center justify-between">
                    <h1 className="text-2xl font-bold">Tenants contract management</h1>

                    <div className="relative group">
                        <HelpCircleIcon className="h-6 w-6 cursor-pointer"/>
                        <div className="absolute top-full mt-2 w-max max-w-xs text-sm text-white bg-gray-800 rounded-md py-2 px-4 opacity-0 group-hover:opacity-100 transition-opacity duration-200 shadow-lg"
                            style={{
                                right: "0",
                                whiteSpace: "normal",
                                overflowWrap: "break-word",
                                transform: "translateX(0)",
                            }}>
                            Tip: Managing Contracts <br />
                            If you need help managing your contracts, such as editing details or generating invoices, please contact the administrator responsible for your account.
                            They can provide assistance and ensure everything runs smoothly.
                        </div>
                    </div>
                </div>
            </header>

            <section className="mx-auto px-4 py-6">
                <PropertiesProvider initialProperties={properties || []}>
                    <TenantsProvider initialTenants={tenants || []}>
                        <ContractProvider initialContracts={contracts || []}>
                            {roles.roles.includes("ROLE_ADMIN") && <ContractPopupForm/>}
                            <ContractList roles={roles.roles}/>
                        </ContractProvider>
                    </TenantsProvider>
                </PropertiesProvider>
            </section>
        </main>
    );
}