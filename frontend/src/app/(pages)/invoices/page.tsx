import {ReactElement} from "react";
import {Invoice, RolesResponse} from "@/types/types";
import {fetchInvoices} from "@/services/invoices";
import InvoiceList from "@/components/invoices/InvoiceList";
import {getAuthToken} from "@/utils/auth";
import {InvoiceProvider} from "@/components/invoices/InvoiceContext";
import {getUserRoles, ValidRoles} from "@/services/global";
import {redirect} from "next/navigation";
import {HelpCircleIcon} from "lucide-react";
import {Metadata} from "next";

export async function generateMetadata(): Promise<Metadata> {
    return {
        title: 'TenantFlow | Invoices',
    };
}

export default async function PropertiesPage(): Promise<ReactElement> {
    const authToken: string | undefined = await getAuthToken();
    const authResponse: RolesResponse | undefined = await getUserRoles(authToken);
    if (!authResponse?.loggedIn) {
        redirect('/login');
    }

    const isPrivileged: boolean = authResponse?.roles?.some((role: ValidRoles): boolean => ["ROLE_ADMIN", "ROLE_MANAGER"].includes(role)) || false;
    const invoices: Invoice[] = await fetchInvoices(authToken) || [];
    return (
        <main className="min-h-screen bg-gray-50">
            <header className="bg-blue-600 text-white py-4 shadow-md">
                <div className="mx-auto px-10 flex items-center justify-between">
                    <h1 className="text-2xl font-bold">Invoice management</h1>

                    <div className="relative group">
                        <HelpCircleIcon className="h-6 w-6 cursor-pointer"/>
                        <div  className="absolute top-full mt-2 w-max max-w-xs text-sm text-white bg-gray-800 rounded-md py-2 px-4 opacity-0 group-hover:opacity-100 transition-opacity duration-200 shadow-lg"
                              style={{
                                 right: "0",
                                 whiteSpace: "normal",
                                 overflowWrap: "break-word",
                                 transform: "translateX(0)",
                            }}>
                            Tip: Need Assistance? <br />
                            If you encounter any issues with invoices or the application, please reach out to the administrator responsible for your account.
                            They can assist you with troubleshooting and resolving your concerns promptly.
                        </div>
                    </div>
                </div>
            </header>

            <section className="mx-auto px-4 py-6">
                <InvoiceProvider initialInvoices={invoices}>
                    <InvoiceList isPrivileged={isPrivileged}/>
                </InvoiceProvider>
            </section>
        </main>
    );
}
