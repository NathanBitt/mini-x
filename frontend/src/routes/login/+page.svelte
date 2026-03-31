<script lang="ts">
	import { onMount } from 'svelte';
	import { goto } from '$app/navigation';
	import type { BackendErrorResponse, LoginResponse } from '$lib/api/types';

	let loginUserName = $state<string>('admin');
	let loginPassword = $state<string>('123');
	let loginLoading = $state<boolean>(false);
	let statusMessage = $state<string | null>(null);
	let errorMessage = $state<string | null>(null);

	const accessTokenKey = 'accessToken';

	function getAuthHeader() {
		const token = localStorage.getItem(accessTokenKey);
		return token ? `Bearer ${token}` : null;
	}

	async function extractBackendError(res: Response) {
		const contentType = res.headers.get('content-type') ?? '';

		if (contentType.includes('application/json')) {
			const data = (await res.json().catch(() => null)) as BackendErrorResponse | null;
			return data?.message ?? data?.error ?? `Request failed (${res.status})`;
		}

		const text = await res.text().catch(() => '');
		return text || res.statusText || `Request failed (${res.status})`;
	}

	async function apiJson<T>(path: string, init?: RequestInit) {
		const res = await fetch(path, init);
		if (!res.ok) {
			const message = await extractBackendError(res);
			throw new Error(message);
		}
		return (await res.json()) as T;
	}

	async function doLogin() {
		errorMessage = null;
		statusMessage = null;
		loginLoading = true;

		try {
			const data = await apiJson<LoginResponse>('/api/login', {
				method: 'POST',
				headers: { 'Content-Type': 'application/json' },
				body: JSON.stringify({ userName: loginUserName, password: loginPassword })
			});

			localStorage.setItem(accessTokenKey, data.accessToken);
			statusMessage = 'Signed in successfully.';
			await goto('/tweets');
		} catch (e) {
			errorMessage = e instanceof Error ? e.message : String(e);
		} finally {
			loginLoading = false;
		}
	}

	onMount(() => {
		if (getAuthHeader()) goto('/tweets');
	});
</script>

<main style="max-width: 900px; margin: 24px auto; padding: 0 16px; font-family: system-ui, sans-serif;">
	<h1 style="margin: 0 0 6px;">Login</h1>
	<p style="margin: 0 0 20px; color: #555;">Use seu usuário e senha para entrar no feed</p>

	{#if errorMessage}
		<div style="background: #fee; border: 1px solid #f99; padding: 10px 12px; border-radius: 8px; margin-bottom: 16px;">
			{errorMessage}
		</div>
	{/if}

	{#if statusMessage}
		<div style="background: #efe; border: 1px solid #9f9; padding: 10px 12px; border-radius: 8px; margin-bottom: 16px;">
			{statusMessage}
		</div>
	{/if}

	<section style="padding: 14px; border: 1px solid #ddd; border-radius: 12px; margin-bottom: 18px;">
		<h2 style="margin: 0 0 10px; font-size: 16px;">Credenciais</h2>

		<form
			onsubmit={(e) => {
				e.preventDefault();
				doLogin();
			}}
			style="display: grid; grid-template-columns: 1fr 1fr auto; gap: 10px; align-items: end;"
		>
			<label style="display: grid; gap: 6px;">
				<span>Username</span>
				<input
					value={loginUserName}
					oninput={(e) => (loginUserName = (e.currentTarget as HTMLInputElement).value)}
					autocomplete="username"
					style="padding: 8px; border-radius: 8px; border: 1px solid #ccc;"
				/>
			</label>

			<label style="display: grid; gap: 6px;">
				<span>Password</span>
				<input
					type="password"
					value={loginPassword}
					oninput={(e) => (loginPassword = (e.currentTarget as HTMLInputElement).value)}
					autocomplete="current-password"
					style="padding: 8px; border-radius: 8px; border: 1px solid #ccc;"
				/>
			</label>

			<button
				type="submit"
				disabled={loginLoading}
				style="padding: 10px 14px; border-radius: 8px; border: none; background: #111; color: #fff; cursor: pointer;"
			>
				{loginLoading ? 'Signing in…' : 'Sign in'}
			</button>
		</form>

		<p style="margin: 14px 0 0; color: #555;">
			Não tem uma conta?
			<a href="/register" style="color: #0a66c2; text-decoration: none;">Crie agora</a>
		</p>
	</section>
</main>

