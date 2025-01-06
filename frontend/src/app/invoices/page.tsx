import {ReactElement} from "react";
import {Invoice, RolesResponse} from "@/types/types";
import {fetchInvoices} from "@/services/invoices";
import InvoiceList from "@/components/invoices/InvoiceList";
import {getAuthToken} from "@/utils/auth";
import {InvoiceProvider} from "@/components/invoices/InvoiceContext";
import {getUserRoles} from "@/services/global";
import {redirect} from "next/navigation";


export default async function PropertiesPage(): Promise<ReactElement> {
    const authToken: string | undefined = await getAuthToken();
    const roles: RolesResponse | undefined = await getUserRoles(authToken);
    if (!roles?.loggedIn) {
        redirect('/login');
    }

    const properties: Invoice[] = await fetchInvoices(authToken) || [];
    return (
        <InvoiceProvider initialInvoices={properties}>
            <InvoiceList />
        </InvoiceProvider>
    );
}
