<script lang="ts">
	import { goto } from '$app/navigation';
	import type { BackendErrorResponse, UserRequest, UserResponse } from '$lib/api/types';

	let registerUserName = $state<string>('');
	let registerPassword = $state<string>('');
	let submitLoading = $state<boolean>(false);
	let statusMessage = $state<string | null>(null);
	let errorMessage = $state<string | null>(null);

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

	async function doRegister() {
		errorMessage = null;
		statusMessage = null;
		submitLoading = true;

		try {
			if (!registerUserName.trim()) throw new Error('Username is required.');
			if (!registerPassword) throw new Error('Password is required.');

			const payload: UserRequest = { userName: registerUserName.trim(), password: registerPassword };
			const _created = await apiJson<UserResponse>('/api/users/new', {
				method: 'POST',
				headers: { 'Content-Type': 'application/json' },
				body: JSON.stringify(payload)
			});

			statusMessage = 'Account created. You can sign in now.';
			await goto('/login');
		} catch (e) {
			errorMessage = e instanceof Error ? e.message : String(e);
		} finally {
			submitLoading = false;
		}
	}
</script>

<main style="max-width: 900px; margin: 24px auto; padding: 0 16px; font-family: system-ui, sans-serif;">
	<h1 style="margin: 0 0 6px;">Crie uma Conta</h1>
	<p style="margin: 0 0 20px; color: #555;">Registre um usuário para acessar o feed de tweets</p>

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
		<h2 style="margin: 0 0 10px; font-size: 16px;">Registrar</h2>

		<form
			onsubmit={(e) => {
				e.preventDefault();
				doRegister();
			}}
			style="display: grid; grid-template-columns: 1fr 1fr; gap: 10px; align-items: end;"
		>
			<label style="display: grid; gap: 6px;">
				<span>Username</span>
				<input
					value={registerUserName}
					oninput={(e) => (registerUserName = (e.currentTarget as HTMLInputElement).value)}
					autocomplete="username"
					style="padding: 8px; border-radius: 8px; border: 1px solid #ccc;"
				/>
			</label>

			<label style="display: grid; gap: 6px;">
				<span>Password</span>
				<input
					type="password"
					value={registerPassword}
					oninput={(e) => (registerPassword = (e.currentTarget as HTMLInputElement).value)}
					autocomplete="new-password"
					style="padding: 8px; border-radius: 8px; border: 1px solid #ccc;"
				/>
			</label>

			<div style="grid-column: span 2; display: flex; gap: 10px; align-items: center;">
				<button
					type="submit"
					disabled={submitLoading}
					style="padding: 10px 14px; border-radius: 8px; border: none; background: #0a66c2; color: #fff; cursor: pointer;"
				>
					{submitLoading ? 'Creating…' : 'Create account'}
				</button>

				<a href="/login" style="color: #0a66c2; text-decoration: none;">De volta para o login</a>
			</div>
		</form>
	</section>
</main>

