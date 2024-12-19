import {Property} from "@/types/types";
import {fetchProperties} from "@/services/properties";
import PropertiesList from "@/components/properties/PropertiesList";
import PropertyPopupForm from "@/components/properties/PropertyPopupForm";

export default async function PropertiesPage() {
    const properties: Property[] = await fetchProperties() || [];
    return (
        <div>
            <PropertyPopupForm />
            {properties.length > 0 && <PropertiesList listOfProperties={properties} />}
        </div>
    );
}
