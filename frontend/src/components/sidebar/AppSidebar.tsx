import {JSX} from "react";
import LogOutButton from "@/components/common/LogOutButton";
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
    }, {
        title: "Properties",
        url: "/properties",
        icon: HousePlugIcon,
    }, {
        title: "Requests",
        url: "/requests",
        icon: GitPullRequestIcon,
    }, {
        title: "Contracts",
        url: "/contracts",
        icon: ReceiptIcon,
    }, {
        title: "Invoices",
        url: "/invoices",
        icon: FileIcon,
    }, {
        title: "Tenants",
        url: "/tenants",
        icon: UserSearchIcon,
    },
];

export function AppSidebar(): JSX.Element {
    return (
        <Sidebar>
            <SidebarContent>
                <SidebarGroup>
                    <SidebarGroupLabel>Application</SidebarGroupLabel>
                    <SidebarGroupContent>
                        <SidebarMenu>
                            {items.map((item) => (
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
                <LogOutButton />
            </SidebarFooter>
        </Sidebar>
    )
}