export interface ConfigState {
	user: User,
	cookie: string
}

export interface User {
	id: string,
	role: string,
	email: string,
}