import {Property} from "@/types/types";
import {fetchProperties} from "@/services/properties";
import PropertiesList from "@/components/properties/PropertiesList";
import PropertyPopupForm from "@/components/properties/PropertyPopupForm";
import {PropertiesProvider} from "@/components/properties/PropertiesContext";

export default async function PropertiesPage() {
    const properties: Property[] = await fetchProperties() || [];

    return (
        <PropertiesProvider initialProperties={properties}>
            <PropertyPopupForm />
            {properties.length > 0 && <PropertiesList />}
        </PropertiesProvider>
    );
}
