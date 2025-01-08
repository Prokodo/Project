import {getAuthToken} from "@/utils/auth";
import {RolesResponse} from "@/types/types";
import {getUserRoles} from "@/services/global";
import {redirect} from "next/navigation";

async function Home() {
  const authToken: string | undefined = await getAuthToken();
  const roles: RolesResponse | undefined = await getUserRoles(authToken);
  if (!roles?.loggedIn) {
    redirect('/login');
  }

  return (
    <div>

    </div>
  );
}

export default Home;