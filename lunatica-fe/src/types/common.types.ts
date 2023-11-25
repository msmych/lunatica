export enum RouteName {
	Login = 'Login',
	Registration = 'Registration',
	Complaints = 'Complaints',
	ComplaintNew = 'ComplaintNew',
	ComplaintsCompleted = 'ComplaintsCompleted',
	Settings = 'Settings',
	Users = 'Users'
}

export enum UserType {
	Admin = 'ADMIN',
	Client = 'CLIENT',
	Worker = 'WORKER',
	Basic = 'BASIC'
}

export enum ApiEndpoints {
	Accounts = 'accounts',
	Login = 'login',
	Me = 'me',
	Complaints = 'complaints'
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

export interface User {
	id: string,
	roles: string[],
	email: string,
}

