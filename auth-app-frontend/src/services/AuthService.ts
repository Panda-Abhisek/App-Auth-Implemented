import api from "@/config/ApiClient";
import type LoginData from "@/models/LoginData";
import type LoginResponseData from "@/models/LoginResponseData";
import type RegisterData from "@/models/RegisterData";
import type User from "@/models/User";

export const registerUser = async (signupData: RegisterData) => {
    const response = await api.post(`/auth/register`, signupData);
    return response.data;
}

export const loginUser = async (loginData: LoginData) => {
    const response = await api.post<LoginResponseData>(`/auth/login`, loginData);
    return response.data;
}

export const logoutUser = async () => {
    const response = await api.post(`/auth/logout`);
    return response.data;
}

//get current login user
export const getCurrentUser = async (emailId: string | undefined) => {
  const response = await api.get<User>(`/users/email/${emailId}`);
  return response.data;
};

//refresh token
export const refreshToken = async () => {
  const response = await api.post<LoginResponseData>(`/auth/refresh`);
  return response.data;
};