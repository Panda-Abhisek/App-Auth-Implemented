import useAuth from "@/auth/store";
import { Alert, AlertTitle } from "@/components/ui/alert";
import { Button } from "@/components/ui/button";
import { Card, CardContent } from "@/components/ui/card";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import { Spinner } from "@/components/ui/spinner";
import type LoginData from "@/models/LoginData";
import { loginUser } from "@/services/AuthService";
import { motion } from "framer-motion";
import { CheckCircle2Icon, Github } from "lucide-react";
import { useState } from "react";
import toast from "react-hot-toast";
import { useNavigate } from "react-router";

export default function Login() {
  const navigate = useNavigate();
  const login = useAuth(state => state.login);

  const [lgoinData, setLoginData] = useState<LoginData>({
    email: "",
    password: "",
  });

  const [loading, setLoading] = useState<boolean>(false);
  const [error, setError] = useState<any>(null);

  const handleInputChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    setLoginData((prev) => ({
      ...prev,
      [event.target.name]: event.target.value,
    }));
  }

  const handleFormSubmit = async (event: React.FormEvent) => {
    event?.preventDefault()
    // validation
    if (lgoinData.email.trim() === '') {
      setError("Email is required!!")
      return;
    }
    if (lgoinData.password.trim() === '') {
      setError("Password is required!!");
      return;
    }

    console.log(lgoinData);
    try {
      setLoading(true);

      //login function : useAuth
      await login(lgoinData)
      // console.log(userInfo);
      
      // const result = await loginUser(lgoinData);
      // console.log("Login user response: ", result);
      toast.success("Login Successfull!")
      setLoginData({
        email: "",
        password: "",
      });
      navigate('/dashboard')
    } catch (error) {
      console.log("Error ", error);
      setError(error);
    } finally {
      setLoading(false);
    }
  }

  return (
    <div className="min-h-screen flex items-center justify-center bg-background px-6">
      <motion.div
        initial={{ opacity: 0, y: 30 }}
        animate={{ opacity: 1, y: 0 }}
        transition={{ duration: 0.6 }}
        className="w-full max-w-md"
      >
        <Card className="bg-card/70 backdrop-blur-xl border-border rounded-2xl shadow-xl">
          <CardContent className="p-8">
            <h1 className="text-3xl font-bold text-center mb-2">
              Welcome Back
            </h1>
            <p className="text-center text-muted-foreground mb-8">
              Log in to continue securing your app
            </p>

            {/* OAuth Buttons */}
            <div className="space-y-3 mb-6">
              <Button
                variant="outline"
                size="lg"
                className="w-full rounded-2xl flex items-center gap-3"
              >
                <img
                  src="https://www.svgrepo.com/show/475656/google-color.svg"
                  alt="Google"
                  className="w-5 h-5"
                />
                Continue with Google
              </Button>

              <Button
                variant="outline"
                size="lg"
                className="w-full rounded-2xl flex items-center gap-3"
              >
                <Github className="w-5 h-5" />
                Continue with GitHub
              </Button>
            </div>

            {/* Divider */}
            <div className="flex items-center gap-4 my-6">
              <div className="flex-1 h-px bg-border" />
              <span className="text-sm text-muted-foreground">
                or continue with
              </span>
              <div className="flex-1 h-px bg-border" />
            </div>

            {/* Alert Section */}
            {error && (
              <div className="mb-4">
                <Alert variant={"destructive"}>
                  <CheckCircle2Icon />
                  <AlertTitle>
                    {error?.response ? error?.response?.data?.message : error}
                  </AlertTitle>
                </Alert>
              </div>
            )}

            {/* Email / Password Form */}
            <form className="space-y-6" onSubmit={handleFormSubmit}>
              <div className="space-y-2">
                <Label htmlFor="email">Email</Label>
                <Input
                  id="email"
                  type="email"
                  placeholder="you@example.com"
                  name="email"
                  value={lgoinData.email}
                  onChange={handleInputChange}
                />
              </div>

              <div className="space-y-2">
                <Label htmlFor="password">Password</Label>
                <Input
                  id="password"
                  type="password"
                  name="password"
                  value={lgoinData.password}
                  onChange={handleInputChange}
                  placeholder="••••••••"
                />
              </div>

              <Button
                type="submit"
                disabled={loading}
                size="lg"
                className="w-full rounded-2xl text-lg"
              >
                {loading ? (
                  <><Spinner /> Logging in..</>
                ) : (
                  "Login"
                )}
              </Button>
            </form>

            <div className="mt-6 text-center text-sm text-muted-foreground">
              Don’t have an account?{" "}
              <a href="/signup" className="text-primary hover:underline">
                Create one
              </a>
            </div>
          </CardContent>
        </Card>
      </motion.div>
    </div>
  );
}
