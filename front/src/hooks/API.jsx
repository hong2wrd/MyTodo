import axios from "axios"
import { getToken, removeToken, setToken } from "./useToken";

const API = axios.create({
    baseURL: 'http://localhost:8080',
    headers: {
        "Content-type": "application/json"
    },
    withCredentials: true
});

API.interceptors.request.use(config => {
    const accessToken = getToken();
    
    if(accessToken) {
        config.headers.Authorization = accessToken;
    }
    return config;
});

API.interceptors.response.use(
    response => {
        return response;
    },
    async error => {
        const originalRequest = error.config;
        
        if (error.response?.status === 401 && !originalRequest._retry) {

            originalRequest._retry = true;

            try {
                const newAccessToken = await refreshAccessToken();
                
                setToken(newAccessToken);
                
                originalRequest.headers.Authorization = newAccessToken

                return API(originalRequest); // 기존 요청을 재요청

            } catch (refreshError) {
                // Refresh Token도 만료
                try {
                    await API.post('/logout');
                
                    removeToken();

                } catch(e) {
                    console.error(e);
                }
                return Promise.reject(refreshError);
            }
        }

        return Promise.reject(error);
    }

);

const refreshAccessToken = async () => {
    const response = await axios.create(
        {
            baseURL: "http://localhost:8080",
            headers: {
                "Content-type": "application/json"
            },
            withCredentials: true
        }
    ).post("/auth/refresh",
        {},
        {
            withCredentials: true
        }
    );

    return response.data;
};

export default API;