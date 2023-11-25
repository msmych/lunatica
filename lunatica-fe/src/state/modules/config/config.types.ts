export interface ConfigState {
	user: User,
	cookie: string
}

export interface User {
	id: string,
	roles: string[],
	email: string,
}