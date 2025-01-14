function UserIndexPage() {
    return (
        <div className="min-h-screen flex flex-col">
            <header className="bg-blue-600 text-white shadow">
                <nav className="p-2 px-10">
                    <div className="flex items-center justify-between">
                        <h1 className="text-2xl font-bold">TenantFlow</h1>
                        <img
                            src="/icon.png"
                            alt="Company Logo"
                            className="w-12 h-12 object-contain"
                        />
                    </div>
                </nav>
            </header>

            <main className="flex-1 flex items-center justify-center bg-blue-50">
                <section className="text-center">
                    <h1 className="text-6xl md:text-8xl font-bold text-blue-600 mb-4">
                        Welcome, Tenants!
                    </h1>
                    <p className="text-lg text-gray-700 mb-6">
                        We're here to make your rental experience smooth and enjoyable.
                    </p>
                    <p className="text-gray-600">
                        Feel free to reach out if you have any questions or need assistance.
                    </p>
                </section>
            </main>

            <footer className="bg-blue-600 text-white py-4">
                <div className="container mx-auto text-center px-4">
                    <p className="text-sm">©2025 Dominik Prokop. All rights reserved.</p>
                </div>
            </footer>
        </div>
    );
}

export default UserIndexPage;