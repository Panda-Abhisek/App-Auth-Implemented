import api from "@/config/ApiClient";
import type LoginData from "@/models/LoginData";
import type LoginResponseData from "@/models/LoginResponseData";
import type RegisterData from "@/models/RegisterData";

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