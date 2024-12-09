"use client"

import {useState} from "react";
import {Property} from "@/types/types";

const PropertiesList = ({ listOfProperties } : { listOfProperties: Property[] }) => {
    const [properties, _] = useState(listOfProperties);

    return (
        <div>
            <h1>Properties List</h1>

            <ul>
                {properties.map((property: Property) => (
                    <li key={property.id}>
                        {property.name} - {property.address}
                    </li>
                ))}
            </ul>
        </div>
    );
};

export default PropertiesList;
