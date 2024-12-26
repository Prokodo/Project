"use client"

import {JSX} from "react";
import Cookies from "js-cookie";
import {LogOutIcon} from "lucide-react";
import {useRouter} from "next/navigation";
import {AppRouterInstance} from "next/dist/shared/lib/app-router-context.shared-runtime";
import {
    AlertDialog, AlertDialogAction, AlertDialogCancel,
    AlertDialogContent, AlertDialogDescription, AlertDialogFooter,
    AlertDialogHeader, AlertDialogTitle, AlertDialogTrigger
} from "@/components/ui/alert-dialog";

const LogOutButton = ({}): JSX.Element => {
    const router: AppRouterInstance = useRouter();

    const handleLogout = (): void => {
        Cookies.remove("authToken");
        router.push("/login");
    };

    return (
        <>
            <AlertDialog>
                <AlertDialogTrigger asChild>
                    <button type="button" className="w-full flex items-center p-2 hover:bg-gray-100 text-gray-800 font-medium transition duration-150 ease-in-out">
                        <LogOutIcon width={16} className="mr-2" />
                        <span>Logout</span>
                    </button>
                </AlertDialogTrigger>

                <AlertDialogContent>
                    <AlertDialogHeader>
                        <AlertDialogTitle>
                            Confirm Logout
                        </AlertDialogTitle>

                        <AlertDialogDescription>
                            Are you sure you want to log out? You will need to sign in again to access your account.
                        </AlertDialogDescription>
                    </AlertDialogHeader>

                    <AlertDialogFooter>
                        <AlertDialogCancel className="px-4 py-2 bg-gray-200 hover:bg-gray-300 text-gray-800 rounded-md transition duration-150 ease-in-out">
                            Cancel
                        </AlertDialogCancel>

                        <AlertDialogAction onClick={handleLogout} className="px-4 py-2 bg-red-600 hover:bg-red-700 text-white rounded-md transition duration-150 ease-in-out">
                            Log Out
                        </AlertDialogAction>
                    </AlertDialogFooter>
                </AlertDialogContent>
            </AlertDialog>
        </>
    );
};

export default LogOutButton;