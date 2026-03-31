export type LoginRequest = {
	userName: string;
	password: string;
};

export type LoginResponse = {
	accessToken: string;
	expiresIn: number;
};

export type UserRequest = {
	userName: string;
	password: string;
};

export type Role = {
	id: number;
	name: string;
};

export type UserResponse = {
	id: string;
	userName: string;
	roles: Role[];
};

export type TweetRequest = {
	post: string;
};

export type FeedTweetResponse = {
	id: number;
	post: string;
	userName: string;
};

export type FeedResponse = {
	feedTweets: FeedTweetResponse[];
	page: number;
	pageSize: number;
	totalPages: number;
	totalElements: number;
};

export type BackendErrorResponse = {
	timesTamp?: string;
	status?: number;
	error?: string;
	message?: string;
};

