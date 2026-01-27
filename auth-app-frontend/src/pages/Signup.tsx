import OAuth2Buttons from "@/components/OAuth2Buttons";
import { Alert, AlertTitle } from "@/components/ui/alert";
import { Button } from "@/components/ui/button";
import { Card, CardContent } from "@/components/ui/card";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import { Spinner } from "@/components/ui/spinner";
import type RegisterData from "@/models/RegisterData";
import { registerUser } from "@/services/AuthService";
import { motion } from "framer-motion";
import { CheckCircle2Icon } from "lucide-react";
import { useState } from "react";
import toast from "react-hot-toast";
import { useNavigate } from "react-router";

function Signup() {

  const navigate = useNavigate();
  const [data, setData] = useState<RegisterData>({
    username: "",
    email: "",
    password: "",
  });

  const [loading, setLoading] = useState<boolean>(false);
  const [error, setError] = useState<any>(null);

  const handleInputChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    // console.log(event.target.name);
    // console.log(event.target.value);
    setData((prev) => ({
      ...prev,
      [event.target.name]: event.target.value,
    }));
  }

  const handleFormSubmit = async (event: React.FormEvent) => {
    event?.preventDefault()
    // validation
    if (data.username.trim() === '') {
      toast.error("Name is required!!");
      setError("Name is required!!")
      return;
    }
    if (data.email.trim() === '') {
      toast.error("Email is required!!");
      setError("Email is required!!");
      return;
    }
    if (data.password.trim() === '') {
      toast.error("Password is required!!");
      setError("Password is required!!");
      return;
    }

    console.log(data);
    try {
      setLoading(true);
      const result = await registerUser(data);
      console.log("Register user response: ", result);
      toast.success("User registered!")
      setData({
        username: "",
        email: "",
        password: "",
      });
      navigate('/login')
    } catch (error) {
      console.log("Error ", error);
      toast.error("User registration failed!!")
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
              Create Your Account
            </h1>
            <p className="text-center text-muted-foreground mb-8">
              Secure your app with next-gen authentication
            </p>

            {/* OAuth Buttons */}
            <OAuth2Buttons />

            {/* Divider */}
            <div className="flex items-center gap-4 my-6">
              <div className="flex-1 h-px bg-border" />
              <span className="text-sm text-muted-foreground">
                or sign up with email
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

            {/* Signup Form */}
            <form className="space-y-5" onSubmit={handleFormSubmit}>
              <div className="space-y-2">
                <Label htmlFor="name">Full Name</Label>
                <Input
                  id="name"
                  type="text"
                  placeholder="John Doe"
                  name="username"
                  value={data.username}
                  onChange={handleInputChange}
                />
              </div>

              <div className="space-y-2">
                <Label htmlFor="email">Email</Label>
                <Input
                  id="email"
                  type="email"
                  placeholder="you@example.com"
                  name="email"
                  value={data.email}
                  onChange={handleInputChange}
                />
              </div>

              <div className="space-y-2">
                <Label htmlFor="password">Password</Label>
                <Input
                  id="password"
                  type="password"
                  name="password"
                  placeholder="••••••••"
                  value={data.password}
                  onChange={handleInputChange}
                />
              </div>

              <div className="space-y-2">
                <Label htmlFor="confirmPassword">Confirm Password</Label>
                <Input
                  id="confirmPassword"
                  type="password"
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
                  <><Spinner /> Creating Account</>
                ) : (
                  "Create Account"
                )}
              </Button>
            </form>

            <div className="mt-6 text-center text-sm text-muted-foreground">
              Already have an account?{" "}
              <a href="/login" className="text-primary hover:underline">
                Login
              </a>
            </div>
          </CardContent>
        </Card>
      </motion.div>
    </div>
  );
}

export default Signup;