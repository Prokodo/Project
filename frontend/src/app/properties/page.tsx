import PropertiesList from "@/components/properties/PropertiesList";
import {fetchProperties} from "@/services/properties";
import {Property} from "@/types/types";
import PropertiesForm from "@/components/properties/PropertiesForm";

export default async function PropertiesPage() {
    const properties: Property[] = await fetchProperties() || [];
    return (
        <div>
            <PropertiesForm />
            {properties.length > 0 && <PropertiesList listOfProperties={properties} />}
        </div>
    );
}
