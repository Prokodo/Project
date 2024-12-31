import {getAuthToken} from "@/utils/auth";
import {RolesResponse} from "@/types/types";
import {getUserRoles} from "@/services/global";
import LogOutAlert from "@/components/auth/LogOutAlert";
import {
    Sidebar, SidebarContent, SidebarFooter,
    SidebarGroup, SidebarGroupContent, SidebarGroupLabel,
    SidebarMenu, SidebarMenuButton, SidebarMenuItem,
} from "@/components/ui/sidebar";
import {LayoutDashboardIcon, HousePlugIcon, GitPullRequestIcon, ReceiptIcon, FileIcon, UserSearchIcon} from "lucide-react";

const items = [
    {
        title: "Home",
        url: "/",
        icon: LayoutDashboardIcon,
        adminOnly: false
    }, {
        title: "Properties",
        url: "/properties",
        icon: HousePlugIcon,
        adminOnly: true
    }, {
        title: "Requests",
        url: "/requests",
        icon: GitPullRequestIcon,
        adminOnly: false
    }, {
        title: "Contracts",
        url: "/contracts",
        icon: ReceiptIcon,
        adminOnly: false
    }, {
        title: "Invoices",
        url: "/invoices",
        icon: FileIcon,
        adminOnly: false
    }, {
        title: "Tenants",
        url: "/tenants",
        icon: UserSearchIcon,
        adminOnly: true
    },
];

export async function AppSidebar() {
    const response: RolesResponse | undefined = await getUserRoles(await getAuthToken());
    const isAdmin: boolean = response?.roles?.includes("ROLE_ADMIN") || false;
    if (!response?.loggedIn) {
        return <></>;
    }

    return (
        <Sidebar>
            <SidebarContent>
                <SidebarGroup>
                    <SidebarGroupLabel>Application</SidebarGroupLabel>
                    <SidebarGroupContent>
                        <SidebarMenu>
                            {items.filter(item => isAdmin || !item.adminOnly).map((item) => (
                                <SidebarMenuItem key={item.title}>
                                    <SidebarMenuButton asChild>
                                        <a href={item.url}>
                                            <item.icon />
                                            <span>{item.title}</span>
                                        </a>
                                    </SidebarMenuButton>
                                </SidebarMenuItem>
                            ))}
                        </SidebarMenu>
                    </SidebarGroupContent>
                </SidebarGroup>
            </SidebarContent>
            <SidebarFooter>
                <LogOutAlert />
            </SidebarFooter>
        </Sidebar>
    )
}