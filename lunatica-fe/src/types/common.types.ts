export enum RouteName {
	Login = 'Login',
	Registration = 'Registration',
	Complaints = 'Complaints', // @ts-ignore
	ComplaintNew = 'ComplaintNew',
	ComplaintsCompleted = 'ComplaintsCompleted',
	Settings = 'Settings',
	Users = 'Users'
}

export enum UserType {
	Admin = 'ADMIN',
	Client = 'CLIENT',
	Worker = 'WORKER'
}

// export interface UserTypeName {
// 	Admin = 'Админ',
// 	Client = 'Клиент',
// 	Worker = 'Работник'
// }

export enum ApiEndpoints {
	Accounts = 'accounts',
	Login = 'login',
	Me = 'me',
	Complaints = 'complaints',
	Info = 'info',
	Messages = 'messages'
}

export interface Complaint {
	id: string,
	problemCountry: {
		name: string
	},
	problemDate: Date,
	type: string,
	content: string,
	status: string
}

export interface ComplaintFull {
	account: {
		email: string
	},
	id: string,
	state: {
		emoji: string,
		nameRu: string,
	},
	problemCountry: {
		emoji: string,
		nameRu: string,
	},
	problemDate: Date,
	type: {
		emoji: string,
		nameRu: string,
	},
	content: string,
	status: string,
	updatedAt: string
}

export interface User {
	id: string,
	roles: string[],
	email: string,
}

export interface State {
	emoji: string,
	nameRu: string,
	code: string
}

export interface Country {
	emoji: string,
	nameRu: string,
	code: string
}

export interface ComplaintType {
	emoji: string,
	nameRu: string,
	code: string
}