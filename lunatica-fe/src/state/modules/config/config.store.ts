import { defineStore } from 'pinia'
import { ConfigState, User } from './config.types'

export const useConfigStore = defineStore('ConfigStore', {
	state: (): ConfigState => ({
		user: {
			id: '',
			role: '',
			email: ''
		},
		cookie: ''
	}),
	actions: {
		setUser(user: User) {
			console.log('user', user)
			this.user = user
		},
		setCookie(cookie: string) {
			this.cookie = cookie
		}
	}
})
