import {ReactElement} from "react";
import {redirect} from "next/navigation";
import {getAuthToken} from "@/utils/auth";
import {getUserRoles} from "@/services/global";
import {fetchProperties} from "@/services/properties";
import {Property, RolesResponse} from "@/types/types";
import RequestsList from "@/components/requests/RequestsList";
import {PropertiesProvider} from "@/components/properties/PropertiesContext";
import {HelpCircleIcon} from "lucide-react";

export default async function PropertiesPage(): Promise<ReactElement> {
    const authToken: string | undefined = await getAuthToken();
    const roles: RolesResponse | undefined = await getUserRoles(authToken);
    if (!roles?.loggedIn) {
        redirect('/login');
    }

    const isAdmin: boolean = roles.roles.includes("ROLE_ADMIN");
    if (!isAdmin) {
        return (
            <>
            </>
        )
    }

    const properties: Property[] = await fetchProperties(authToken) || [];
    return (
        <main className="min-h-screen bg-gray-50">
            <header className="bg-blue-600 text-white py-4 shadow-md">
                <div className="mx-auto px-10 flex items-center justify-between">
                    <h1 className="text-2xl font-bold">Requests management</h1>

                    <div className="relative group">
                        <HelpCircleIcon className="h-6 w-6 cursor-pointer"/>
                        <div
                            className="absolute top-full mt-2 w-max max-w-xs text-sm text-white bg-gray-800 rounded-md py-2 px-4 opacity-0 group-hover:opacity-100 transition-opacity duration-200 shadow-lg"
                            style={{
                                right: "0",
                                whiteSpace: "normal",
                                overflowWrap: "break-word",
                                transform: "translateX(0)",
                            }}>
                            Tip: Managing Properties <br/>
                            If you need help managing your properties—such as adding new listings, updating details, or
                            addressing maintenance requests—please
                            contact the administrator assigned to your account. They can assist you and ensure
                            everything is handled effectively.
                        </div>
                    </div>
                </div>
            </header>

            <section className="mx-auto px-4 py-6">
                <PropertiesProvider initialProperties={properties}>
                    <RequestsList roles={roles.roles}/>
                </PropertiesProvider>
            </section>
        </main>
    );
}