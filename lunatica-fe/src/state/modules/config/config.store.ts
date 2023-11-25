import { defineStore } from 'pinia'
import { ConfigState } from './config.types'
import { User } from './../../../types/common.types'

export const useConfigStore = defineStore('ConfigStore', {
	state: (): ConfigState => ({
		user: {
			id: '',
			roles: [],
			email: ''
		},
		cookie: ''
	}),
	actions: {
		setUser(user: User) {
			this.user = user
		},
		setCookie(cookie: string) {
			this.cookie = cookie
		}
	},
	persist: true
})
