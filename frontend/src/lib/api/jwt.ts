type JwtPayload = Record<string, unknown>;

function base64UrlDecode(input: string) {
	// Convert base64url -> base64
	const base64 = input.replace(/-/g, '+').replace(/_/g, '/');
	// Pad to a multiple of 4 for atob
	const pad = base64.length % 4 === 0 ? '' : '='.repeat(4 - (base64.length % 4));
	return atob(base64 + pad);
}

export function decodeJwtPayload(token: string): JwtPayload | null {
	const parts = token.split('.');
	if (parts.length < 2) return null;

	try {
		const json = base64UrlDecode(parts[1]);
		return JSON.parse(json) as JwtPayload;
	} catch {
		return null;
	}
}

export function tokenHasAdminScope(token: string): boolean {
	const payload = decodeJwtPayload(token);
	if (!payload) return false;

	const scope = payload['scope'];
	const adminMarkers = new Set(['admin', 'SCOPE_admin']);

	if (Array.isArray(scope)) {
		return scope.some((s) => typeof s === 'string' && adminMarkers.has(s));
	}

	if (typeof scope === 'string') {
		// Some implementations may store as a space-delimited string.
		return scope.split(/[ ,]+/).some((s) => adminMarkers.has(s));
	}

	return false;
}

