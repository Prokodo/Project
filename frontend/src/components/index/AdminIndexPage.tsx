function AdminIndexPage() {
    return (
        <div className="min-h-screen flex flex-col">
            <header className="bg-blue-600 text-white shadow">
                <nav className="p-2 px-10">
                    <div className="flex items-center justify-between content-between">
                        <h1 className="text-2xl font-bold">TenantFlow</h1>
                        <img src="/icon.png" alt="Company Logo" className="w-12 h-12 ml-2 object-contain" />
                    </div>
                </nav>
            </header>

            <main className="flex-1">
                <section className="bg-blue-50 py-32 text-center">
                    <div className="container mx-auto px-4">
                        <h1 className="text-4xl md:text-6xl font-bold text-blue-600 mb-4">
                            Welcome to TenantManager
                        </h1>

                        <p className="text-lg text-gray-700 mb-8">
                            Your comprehensive solution for efficient tenant and property management.
                        </p>

                        <a href="#introduction"
                           className="bg-blue-600 text-white px-6 py-3 rounded-md text-lg shadow-md hover:bg-blue-700">
                            Discover More
                        </a>
                    </div>
                </section>

                <section id="introduction" className="py-20 bg-white">
                    <div className="container mx-auto px-4">
                        <h2 className="text-3xl font-bold text-gray-800 text-center mb-12">
                            About TenantManager
                        </h2>

                        <p className="text-lg text-gray-700 max-w-3xl mx-auto text-center">
                            TenantFlow is designed to streamline your property management tasks, allowing you to focus
                            on what matters most – providing excellent service to your tenants and growing your business.
                            Navigate through our application to manage tenants, properties, invoices, and more with ease.
                        </p>
                    </div>
                </section>

                <section id="features" className="py-20 bg-gray-50">
                    <div className="container mx-auto px-4">
                        <h2 className="text-3xl font-bold text-gray-800 text-center mb-12">
                            Our Features
                        </h2>
                        <div className="grid md:grid-cols-3 gap-8">
                            <div
                                className="p-6 bg-white rounded-lg shadow hover:shadow-lg transition-shadow duration-300">
                                <div className="text-blue-600 text-4xl mb-4">
                                    🧑‍💼
                                </div>
                                <h3 className="text-xl font-semibold mb-2">Tenant Management</h3>
                                <p className="text-gray-600">
                                    Easily add, edit, and manage tenant information, track leases, and monitor tenant
                                    interactions.
                                </p>
                            </div>

                            <div
                                className="p-6 bg-white rounded-lg shadow hover:shadow-lg transition-shadow duration-300">
                                <div className="text-blue-600 text-4xl mb-4">
                                    🏢
                                </div>
                                <h3 className="text-xl font-semibold mb-2">Property Listings</h3>
                                <p className="text-gray-600">
                                    Organize and showcase all your properties with detailed descriptions, images, and
                                    availability status.
                                </p>
                            </div>

                            <div
                                className="p-6 bg-white rounded-lg shadow hover:shadow-lg transition-shadow duration-300">
                                <div className="text-blue-600 text-4xl mb-4">
                                    💰
                                </div>
                                <h3 className="text-xl font-semibold mb-2">Invoice and Contract Tracking</h3>
                                <p className="text-gray-600">
                                    Monitor rent payments or invoices to keep your business finances in check.
                                </p>
                            </div>

                            <div
                                className="p-6 bg-white rounded-lg shadow hover:shadow-lg transition-shadow duration-300">
                                <div className="text-blue-600 text-4xl mb-4">
                                    🛠️
                                </div>
                                <h3 className="text-xl font-semibold mb-2">Maintenance Requests</h3>
                                <p className="text-gray-600">
                                    Streamline maintenance by allowing tenants to submit requests and track their status
                                    in real-time.
                                </p>
                            </div>

                            <div
                                className="p-6 bg-white rounded-lg shadow hover:shadow-lg transition-shadow duration-300">
                                <div className="text-blue-600 text-4xl mb-4">
                                    🔔
                                </div>
                                <h3 className="text-xl font-semibold mb-2">Automated Notifications</h3>
                                <p className="text-gray-600">
                                    Keep tenants informed with automated emails and SMS notifications for important
                                    updates and reminders.
                                </p>
                            </div>

                            <div
                                className="p-6 bg-white rounded-lg shadow hover:shadow-lg transition-shadow duration-300">
                                <div className="text-blue-600 text-4xl mb-4">
                                    📊
                                </div>
                                <h3 className="text-xl font-semibold mb-2">Analytics & Reporting</h3>
                                <p className="text-gray-600">
                                    Gain insights into your property performance with comprehensive analytics and
                                    customizable reports.
                                </p>
                            </div>
                        </div>
                    </div>
                </section>
            </main>

            <footer className="bg-blue-600 text-white py-6">
                <div className="container mx-auto text-center px-4">
                    <p className="text-sm">©2025 Dominik Prokop. All rights reserved.</p>
                </div>
            </footer>
        </div>
    );
}

export default AdminIndexPage;