import axios from "axios"

const Authorization = "Authorization";

export const setAccessToken = token => {
    localStorage.setItem(Authorization, token);
};

export const getAccessToken = () => {
    return localStorage.getItem(Authorization);
};

const API = axios.create({
    baseURL: 'http://localhost:8080',
    headers: {
        "Content-type": "application/json"
    }
});

API.interceptors.request.use(config => {
    const accessToken = localStorage.getItem(Authorization)
    
    if(accessToken) {
        config.headers.Authorization = accessToken;
    }
    return config;
});

export default API;